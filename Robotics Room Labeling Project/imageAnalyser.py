import colorsys
import math
import string
import random
import Image
from sys import *
from sets import *

BRIGHTNESS_CHANGE_THRESHOLD=0.008
WEIGHTED_BRIGHTNESS_CHANGE_THRESHOLD=0.20
NEARBY_EDGE_PERCENTAGE_THRESHOLD=0.20
PIXEL_MANHATTAN_DISTANCE_THRESHOLD=2
LINE_LOOKAHEAD_THRESHOLD=100
LINE_WIDTH_THRESHOLD=3

FLOOR_REGION_COLOR=(255, 165, 0)
CEILING_REGION_COLOR=(255, 0, 0)
EDGE_PIXEL=(0, 0, 255)
LINE_PIXEL=(255, 255, 0)

#Object which is used to analyse an input room image for its walls, floor, ceiling, and corners between room planes
class ImageAnalyser:
    def __init__(self, image_file):
        self.original_image_file=image_file
        self.original_image=Image.open(self.original_image_file, "r")
        self.original_pixels=self.original_image.load()
        self.smoothed_edge_image=None
        self.smoothed_edge_pixels=None
        self.edge_pixels=[]
        self.width=self.original_image.size[0]
        self.height=self.original_image.size[1]

        print "Image Dimensions: %d %d" % (self.width, self.height)


    def get_neighboring_pixels(self, pixels, i, j, width, height):
        neighboring_pixels=[]
        if i>0:
            if j>0:
	        neighboring_pixels.append(pixels[i-1, j-1])
            if j<height-1:
                neighboring_pixels.append(pixels[i-1, j+1])
            neighboring_pixels.append(pixels[i-1, j])
        if i<width-1:
            if j>0:
                neighboring_pixels.append(pixels[i+1, j-1])
            if j<height-1:
                neighboring_pixels.append(pixels[i+1, j+1])
            neighboring_pixels.append(pixels[i+1, j])
        if j>0:
            neighboring_pixels.append(pixels[i, j-1])
        if j<height-1:
            neighboring_pixels.append(pixels[i, j+1])

        return neighboring_pixels


    #Returns statistics on the number of edge pixels surrounding a given edge pixel in the analysed room image 
    def get_surrounding_edge_pixel_statistics(self, pixel_list, row, column):
        number_of_edge_pixels=0
        region_width=min(column+PIXEL_MANHATTAN_DISTANCE_THRESHOLD, self.width)-max(column-PIXEL_MANHATTAN_DISTANCE_THRESHOLD, 0)+1
        region_height=min(row+PIXEL_MANHATTAN_DISTANCE_THRESHOLD, self.height)-max(row-PIXEL_MANHATTAN_DISTANCE_THRESHOLD, 0)+1

        for current_row in xrange(max(row-PIXEL_MANHATTAN_DISTANCE_THRESHOLD, 0), min(row+PIXEL_MANHATTAN_DISTANCE_THRESHOLD+1, self.height)):
            for current_column in xrange(max(column-PIXEL_MANHATTAN_DISTANCE_THRESHOLD, 0), min(column+PIXEL_MANHATTAN_DISTANCE_THRESHOLD+1, self.width)):
                if pixel_list[current_column, current_row]==EDGE_PIXEL:
                    number_of_edge_pixels+=1

        return (number_of_edge_pixels, region_width*region_height)


    def get_average_color(self, pixel_list):
        average_color=[0, 0, 0]
        for pixel in pixel_list:
            for index in xrange(len(average_color)):
                average_color[index]+=pixel[index]
        for index in xrange(len(average_color)):
            average_color[index]/=len(pixel_list)

        return tuple(average_color)


    def is_approximately_black(self, rgb_tuple):
        return rgb_tuple[0]<40 and rgb_tuple[1]<40 and rgb_tuple[2]<40


    def is_approximately_yellow(self, rgb_tuple, red_green_factor, blue_factor):
        return rgb_tuple[0]>red_green_factor*255 and rgb_tuple[1]>red_green_factor*255 and rgb_tuple[2]<blue_factor*255


    #Gets the "weighted brightness" of an image pixel based on a filter and the brightness of itself and its neighbors
    def get_weighted_brightness(self, filter, row, column):
        weighted_brightness=0
        for row_offset in xrange(-1, len(filter)-1):
            for column_offset in xrange(-1, len(filter[0])-1):
                brightness_value=colorsys.rgb_to_hsv(self.original_pixels[column+column_offset, row+row_offset][0]/255.0, self.original_pixels[column+column_offset, row+row_offset][1]/255.0, self.original_pixels[column+column_offset, row+row_offset][2]/255.0)[2]
                weighted_brightness+=filter[row_offset+1][column_offset+1]*brightness_value

        return weighted_brightness


    #Gets the linear regression of a group of edge pixels in order to try and draw a solid line closely matching them in the image
    def get_linear_regression(self, pixel_list):
        x_average=0
        y_average=0
        x_differences_squares_sum=0
        xy_differences_product_sum=0
        for pixel in pixel_list:
            x_average+=pixel[0]
            y_average+=pixel[1]
        x_average/=float(len(pixel_list))
        y_average/=float(len(pixel_list))

        for pixel in pixel_list:
            x_differences_squares_sum+=(pixel[0]-x_average) ** 2
            xy_differences_product_sum+=(pixel[0]-x_average)*(pixel[1]-y_average)

        #Vertical line case
        if x_differences_squares_sum==0:
            b1=None #Vertical line has no slope
            b0=x_average #Keep track of x-value vertical line rides along
        else:
            b1=xy_differences_product_sum/float(x_differences_squares_sum)
            b0=y_average-b1*x_average

        return b0, b1


    """
    Adds a wall delineation line to the image based on its previously calculated linear regression.
    b0 corresponds to the y-intercept of this line, and b1 corresponds to its slope (in the coordinate 
    space for this particular line, y increases as one moves down in the image, but the linear regression
    algorithm from earlier still works in this type of situation).
    """
    def add_line(self, b0, b1):
        if b1==None: #Verical line case
            for value in xrange(0, self.height):
                self.analysed_pixels[int(b0), value]=LINE_PIXEL
                if (value, int(b0)) in self.edge_pixels:
                    self.edge_pixels.remove((value, int(b0)))
                for offset in xrange(max(0, int(b0)-LINE_WIDTH_THRESHOLD), min(int(b0)+LINE_WIDTH_THRESHOLD+1, self.width)):
                    self.analysed_pixels[offset, value]=LINE_PIXEL
                    if (value, offset) in self.edge_pixels:
                        self.edge_pixels.remove((value, offset))
        else:
            #Loop for drawing line depends on its slope (to ensure smoothness)
            if math.fabs(b1)<=1:
                for value in xrange(0, self.width):
                    if int(b0+b1*value)>=0 and int(b0+b1*value)<self.height:
                        self.analysed_pixels[value, int(b0+b1*value)]=LINE_PIXEL
                        if (int(b0+b1*value), value) in self.edge_pixels:
                            self.edge_pixels.remove((int(b0+b1*value), value))
                        for offset in xrange(max(0, value-LINE_WIDTH_THRESHOLD), min(value+LINE_WIDTH_THRESHOLD+1, self.width)):
                            self.analysed_pixels[offset, int(b0+b1*value)]=LINE_PIXEL
                            if (int(b0+b1*value), offset) in self.edge_pixels:
                                self.edge_pixels.remove((int(b0+b1*value), offset))
            else:
                for y in xrange(0, self.height):
                    if int((y-b0)/float(b1))>=0 and int((y-b0)/float(b1))<self.width:
                        self.analysed_pixels[int((y-b0)/float(b1)), y]=LINE_PIXEL
                        if (y, int((y-b0)/float(b1))) in self.edge_pixels:
                            self.edge_pixels.remove((y, int((y-b0)/float(b1))))
                        for offset in xrange(max(0, y-LINE_WIDTH_THRESHOLD), min(y+LINE_WIDTH_THRESHOLD+1, self.height)):
                            self.analysed_pixels[int((y-b0)/float(b1)), offset]=LINE_PIXEL
                            if (offset, int((y-b0)/float(b1))) in self.edge_pixels:
                                self.edge_pixels.remove((offset, int((y-b0)/float(b1))))


    """
    Label an area surrounding a given pixel with a certain color in order to label it as a particular type of image region.
    Usually, this algorithm tries to eliminate "black" pixels (to label wall regions), but this algorithm can also try to
    spread pixels of a certain color onto other pixels of a very specific color in order to label the ceiling and/or floor in
    the room image.
    """
    def label_surrounding_area(self, pixels, color, column, row, width, height, color_to_look_for=None):
        pixel_queue=[]
        pixel_set=Set()
        if column>0 and ((self.is_approximately_black(pixels[column-1, row]) and color_to_look_for==None) or pixels[column-1, row]==color_to_look_for):
            pixel_queue.append((column-1, row))
            pixel_set.add((column-1, row))
        if column<width-1 and ((self.is_approximately_black(pixels[column+1, row]) and color_to_look_for==None) or pixels[column+1, row]==color_to_look_for):
            pixel_queue.append((column+1, row))
            pixel_set.add((column+1, row))
        if row>0 and ((self.is_approximately_black(pixels[column, row-1]) and color_to_look_for==None) or pixels[column, row-1]==color_to_look_for):
            pixel_queue.append((column, row-1))
            pixel_set.add((column, row-1))
        if row<height-1 and ((self.is_approximately_black(pixels[column, row+1]) and color_to_look_for==None) or pixels[column, row+1]==color_to_look_for):
            pixel_queue.append((column, row+1))
            pixel_set.add((column, row+1))

        #Use BFS up to the yellow-colored walls delineating boundaries between different image regions
        while len(pixel_queue)>0:
            current_pixel_coordinates=pixel_queue.pop(0)
            current_column=current_pixel_coordinates[0]
            current_row=current_pixel_coordinates[1]
            pixels[current_pixel_coordinates[0], current_pixel_coordinates[1]]=color
            if current_column>0 and ((self.is_approximately_black(pixels[current_column-1, current_row]) and color_to_look_for==None) or pixels[current_column-1, current_row]==color_to_look_for) and not (current_column-1, current_row) in pixel_set:
                pixel_queue.append((current_column-1, current_row))
                pixel_set.add((current_column-1, current_row))
            if current_column<width-1 and ((self.is_approximately_black(pixels[current_column+1, current_row]) and color_to_look_for==None) or pixels[current_column+1, current_row]==color_to_look_for) and not (current_column+1, current_row) in pixel_set:
                pixel_queue.append((current_column+1, current_row))
                pixel_set.add((current_column+1, current_row))
            if current_row>0 and ((self.is_approximately_black(pixels[current_column, current_row-1]) and color_to_look_for==None) or pixels[current_column, current_row-1]==color_to_look_for) and not (current_column, current_row-1) in pixel_set:
                pixel_queue.append((current_column, current_row-1))
                pixel_set.add((current_column, current_row-1))
            if current_row<height-1 and ((self.is_approximately_black(pixels[current_column, current_row+1]) and color_to_look_for==None) or pixels[current_column, current_row+1]==color_to_look_for) and not (current_column, current_row+1) in pixel_set:
                pixel_queue.append((current_column, current_row+1))
                pixel_set.add((current_column, current_row+1))

        return pixels


    #Try to eliminate noise in the input image to avoid potential wall misclassifications
    def smooth_image(self):
        self.analysed_image=Image.new('RGB', (self.width, self.height), "black")
        self.analysed_pixels=self.analysed_image.load()

        for column in xrange(self.width):
            for row in xrange(self.height):
                #Each pixel is simply set to the average value of the original colors of its neighbors
                self.analysed_pixels[column, row]=self.get_average_color(self.get_neighboring_pixels(self.original_pixels, column, row, self.width, self.height))


    #Try to label pixels which are part of wall edges by observing the rate of brightness change from pixel to pixel
    def label_edges(self):
        previous_brightness=0
        self.original_analysed_pixels={}

        for row in xrange(self.height):
            for column in xrange(self.width):
                brightness=colorsys.rgb_to_hsv(self.analysed_pixels[column, row][0]/255.0, self.analysed_pixels[column, row][1]/255.0, self.analysed_pixels[column, row][2]/255.0)[2]
                if math.fabs(brightness-previous_brightness)>=BRIGHTNESS_CHANGE_THRESHOLD:
                    self.original_analysed_pixels[row, column]=self.analysed_pixels[column, row]
                    self.analysed_pixels[column, row]=EDGE_PIXEL
                    self.edge_pixels.append((row, column))
                previous_brightness=brightness


    """
    Try to label pixels which are part of wall edges by passing pixels (and their neighbors) through filters
    designed to catch horizontal and vertical edges in the room image
    """
    def label_edges_with_filters(self):
        sobel_horizontal_filter=[[1, 2, 1], [0, 0, 0], [-1, -2, -1]]
        sobel_vertical_filter=[[-1, 0, 1], [-2, 0, 2], [-1, 0, 1]]
        self.original_analysed_pixels={}

        #Check for horizontal edges
        for row in xrange(1, self.height-1):
            for column in xrange(1, self.width-1):
                weighted_horizontal_brightness=self.get_weighted_brightness(sobel_horizontal_filter, row, column)
                if math.fabs(weighted_horizontal_brightness)>=WEIGHTED_BRIGHTNESS_CHANGE_THRESHOLD:
                    self.original_analysed_pixels[row, column]=self.analysed_pixels[column, row]
                    self.analysed_pixels[column, row]=EDGE_PIXEL
                    self.edge_pixels.append((row, column))

        #Check for vertical edges
        for row in xrange(1, self.height-1):
            for column in xrange(1, self.width-1):
                if self.analysed_pixels[column, row]==EDGE_PIXEL:
                    continue
                weighted_vertical_brightness=self.get_weighted_brightness(sobel_vertical_filter, row, column)
                if math.fabs(weighted_vertical_brightness)>=WEIGHTED_BRIGHTNESS_CHANGE_THRESHOLD:
                    self.original_analysed_pixels[row, column]=self.analysed_pixels[column, row]
                    self.analysed_pixels[column, row]=EDGE_PIXEL
                    self.edge_pixels.append((row, column))


    #Once the edge pixels have been found, try to eliminate any isolated ones which generate "noise"
    def smooth_edge_noise(self):
        edge_pixels_to_filter_out=[]
        for row in xrange(self.height):
            for column in xrange(self.width):
                if self.analysed_pixels[column, row]==EDGE_PIXEL:
                    surrounding_edge_pixel_statistics=self.get_surrounding_edge_pixel_statistics(self.analysed_pixels, row, column)
                    if float(surrounding_edge_pixel_statistics[0])/surrounding_edge_pixel_statistics[1]<NEARBY_EDGE_PERCENTAGE_THRESHOLD:
                        edge_pixels_to_filter_out.append((row, column))

        for pixel in edge_pixels_to_filter_out:
            row=pixel[0]
            column=pixel[1]
            self.analysed_pixels[column, row]=self.original_analysed_pixels[row, column]
            self.edge_pixels.remove((row, column))


    #Tries to create solid lines for the approximate edges formed by the edge pixels found earlier
    def solidify_edge_lines(self):
        line_point_sets=[]
        while len(self.edge_pixels)>0:
            current_pixel=self.edge_pixels[random.randint(0, len(self.edge_pixels)-1)] #Choose a random pixel to do regression around
            current_pixel_row=current_pixel[0]
            current_pixel_column=current_pixel[1]

            number_of_pixels_in_horizontal=0
            number_of_pixels_in_vertical=0
            horizontal_region_pixels=[]
            vertical_region_pixels=[]
            
            #See how many points are within a narrow horizontal as well as a narrow vertical region surrounding the chosen pixel
            for column in xrange(max(0, current_pixel_column-LINE_WIDTH_THRESHOLD), min(self.width, current_pixel_column+LINE_WIDTH_THRESHOLD+1)):
                for row in xrange(max(0, current_pixel_row-LINE_LOOKAHEAD_THRESHOLD), min(self.height, current_pixel_row+LINE_LOOKAHEAD_THRESHOLD+1)):
                    if self.analysed_pixels[column, row]==EDGE_PIXEL:
                        number_of_pixels_in_vertical+=1
                        vertical_region_pixels.append((column, row))
                        
            for column in xrange(max(0, current_pixel_column-LINE_LOOKAHEAD_THRESHOLD), min(self.width, current_pixel_column+LINE_LOOKAHEAD_THRESHOLD+1)):
                for row in xrange(max(0, current_pixel_row-LINE_WIDTH_THRESHOLD), min(self.height, current_pixel_row+LINE_WIDTH_THRESHOLD+1)):
                    if self.analysed_pixels[column, row]==EDGE_PIXEL:
                        number_of_pixels_in_horizontal+=1
                        horizontal_region_pixels.append((column, row))

            if max(number_of_pixels_in_vertical, number_of_pixels_in_horizontal)==1: #No useful regression can be obtained here
                self.edge_pixels.remove(current_pixel)
            else:
                if number_of_pixels_in_vertical>=number_of_pixels_in_horizontal:
                    b0, b1=self.get_linear_regression(vertical_region_pixels)
                else:
                    b0, b1=self.get_linear_regression(horizontal_region_pixels)
                self.add_line(b0, b1)

                #If regression line doesn't go through chosen pixel, delete it anyway so algorithm will eventually terminate
                if current_pixel in self.edge_pixels:
                    self.edge_pixels.remove(current_pixel)


    #Smooths the colors of the edges of an input "wireframe" image used to test my planar region classification algorithm
    def smooth_edge_color(self, input_file, red_green_factor, blue_factor):
        image=Image.open(input_file, "r")
        pixels=image.load()
        width=image.size[0]
        height=image.size[1]

        self.smoothed_edge_image=Image.new('RGB', (width, height), "black")
        self.smoothed_edge_pixels=self.smoothed_edge_image.load()

        for column in xrange(width):
            for row in xrange(height):
                current_pixel=pixels[column, row]
		if self.is_approximately_yellow(current_pixel, red_green_factor, blue_factor):
                    self.smoothed_edge_pixels[column, row]=(255, 255, 0)

        return width, height


    """
    Colors in different planar regions of a wireframe image based on whether or not they are likely 
    to be walls, the floor, or the ceiling. The final result is saved to an output file. Ceilings
    are colored red, while the floor is colored orange. It is assumed that the "smooth_edge_color"
    function is called before this function on a "wireframe" image.
    """
    def delineate_image_regions(self, output_file_name, width, height):
        image=self.smoothed_edge_image
        pixels=self.smoothed_edge_pixels
        width=image.size[0]
        height=image.size[1]

        delineated_image=Image.new('RGB', (width, height), "black")
        delineated_pixels=delineated_image.load()

        for column in xrange(width):
            for row in xrange(height):
                delineated_pixels[column, row]=pixels[column, row]

        #Color in different planes of the image using BFS
        for column in xrange(width):
            for row in xrange(height):
                current_pixel=delineated_pixels[column, row]
                if self.is_approximately_black(current_pixel):
                    region_color=(0, 0, 0)
                    while self.is_approximately_black(region_color):
                        region_color=(random.randint(0, 255), random.randint(0, 255), random.randint(0, 255))
                    delineated_pixels[column, row]=region_color
                    delineated_pixels=self.label_surrounding_area(delineated_pixels, region_color, column, row, width, height)

        floor_present=False
        ceiling_present=False
        colors_seen_on_top=[]
        colors_seen_on_bottom=[]

        #Label the ceiling region if it exists
        for column in xrange(width):
            if (delineated_pixels[column, 0] not in colors_seen_on_top) and not self.is_approximately_yellow(delineated_pixels[column, 0], 0.80, 0.20):
                colors_seen_on_top.append(delineated_pixels[column, 0])

        #Ceiling is assumed to be "middle" region of an odd number of regions at the top of the image (or the only region on top)
        if len(colors_seen_on_top)%2==0:
            ceiling_present=False
        else:
            ceiling_color=colors_seen_on_top[len(colors_seen_on_top)/2]
            for column in xrange(width):
                if delineated_pixels[column, 0]==ceiling_color:
                    delineated_pixels[column, 0]=CEILING_REGION_COLOR
                    self.label_surrounding_area(delineated_pixels, CEILING_REGION_COLOR, column, 0, width, height, ceiling_color)

        #Label the floor region if it exists
        for column in xrange(width):
            if (delineated_pixels[column, height-1] not in colors_seen_on_bottom) and not self.is_approximately_yellow(delineated_pixels[column, height-1], 0.80, 0.20):
                colors_seen_on_bottom.append(delineated_pixels[column, height-1])

        #Floor is assumed to be "middle" region of an odd number of regions at the bottom of the image (or the only bottom region)
        if len(colors_seen_on_bottom)%2==0:
            floor_present=False
        else:
            floor_color=colors_seen_on_bottom[len(colors_seen_on_bottom)/2]
            for column in xrange(width):
                if delineated_pixels[column, height-1]==floor_color:
                    delineated_pixels[column, height-1]=FLOOR_REGION_COLOR
                    self.label_surrounding_area(delineated_pixels, FLOOR_REGION_COLOR, column, height-1, width, height, floor_color)

        delineated_image.save("Image Region Labelings/"+output_file_name+("" if output_file_name.endswith(".jpg") else ".jpg"))


    #Saves the final analysed image
    def save_final_image(self):
        self.analysed_image.save("Image Results/"+string.split(self.original_image_file, "/")[-1])


#Starts the program
if __name__=="__main__":
    if len(argv)!=2:
        print "Error, call format is: %s <input_image>" % (argv[0],)
        exit(1)

    image_analyser=ImageAnalyser("Input Images/"+argv[1]+("" if argv[1].endswith(".jpg") else ".jpg"))
    image_analyser.smooth_image()
    image_analyser.label_edges_with_filters()
    image_analyser.smooth_edge_noise()
    image_analyser.save_final_image()

    #width, height=image_analyser.smooth_edge_color("Wireframes for Wall Type Test Images/"+argv[1]+("" if argv[1].endswith(".jpg") else ".jpg"), 0.20, 0.80)
    #image_analyser.delineate_image_regions(argv[1]+("" if argv[1].endswith(".jpg") else ".jpg"), width, height)

    print "Done"

