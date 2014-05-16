import pyttsx
import pygame
import pygame.gfxdraw
from pygame.locals import *
from sys import exit
from time import sleep
import math
import random

WALL_THICKNESS=20
WIDTH=600
HEIGHT=600

fruit_image_filenames=['apple.png', 'bannana.png', 'cherry.png', 'orange.png', 'pair.png', 'watermelon.png']
ghost_image_filenames=['red_ghost.png', 'yellow_ghost.png']
fruit_sound_files=["C4.wav", "E4.wav", "G4.wav", "Bb4.wav", "C5.wav", "E5.wav", "G5.wav", "Bb5.wav"]
ghost_sound_files=["F2.wav", "A2.wav"]
number_of_fruits=[2, 3, 4, 5, 6, 8] #Number of fruits in different levels
grid_sizes=[8, 9, 10, 12, 14, 16]
level_colors=[(255, 0, 0), (0, 0, 255), (255, 255, 0), (0, 220, 220), (255, 165, 0), (0, 255, 0)]

class GameObject:
    def __init__(self):
        pygame.init()
        pygame.mixer.init(buffer=1024)
        pygame.mixer.set_num_channels(13)
        pygame.mixer.set_reserved(13)
        self.engine=pyttsx.init()

        self.eating_sound=pygame.mixer.Sound("sounds/eat.wav")
        self.hit_sound=pygame.mixer.Sound("sounds/ghost_hit.wav")
        self.bump_sound=pygame.mixer.Sound("sounds/bump.wav")
        self.misc_sound_channel=pygame.mixer.Channel(10)
        self.misc_sound_channel.set_volume(1.0, 1.0)
        
        self.screen = pygame.display.set_mode((WIDTH, HEIGHT), 0, 32)
        self.font=pygame.font.SysFont(pygame.font.get_default_font(), 72)
        self.key_held_down=False
        self.pacman_visible=True
        
        self.leftWall=pygame.Rect(0, 0, WALL_THICKNESS, HEIGHT)
        self.topWall=pygame.Rect(0+WALL_THICKNESS, 0, WIDTH-WALL_THICKNESS, WALL_THICKNESS)
        self.rightWall=pygame.Rect(WIDTH-WALL_THICKNESS, 0+WALL_THICKNESS, WALL_THICKNESS, HEIGHT-WALL_THICKNESS)
        self.bottomWall=pygame.Rect(0+WALL_THICKNESS, HEIGHT-WALL_THICKNESS, WIDTH-2*WALL_THICKNESS, WALL_THICKNESS)

        self.level=1
        self.score=0
        self.lives=4
        self.engine.say('Welcome to Fruit Man! You are wandering in a field and very hungry.')
        self.engine.say('Try to collect all of the fruits using the sounds in your ears, but')
        self.engine.say('don\'t hit the ghosts! They want to eat you as well. Good luck!')
        self.engine.runAndWait()
        self.gameRunning=True
        self.gameJustEnded=False

    def start_level(self, justFinishedPreviousLevel=False):
        self.grid_size=grid_sizes[self.level-1] #Width and height (in cells) of grid pacman runs around
        self.cell_size=(WIDTH-2.0*WALL_THICKNESS)/self.grid_size
        
        self.fruit_sound_channels=[]
        self.ghost_sound_channels=[]
        pygame.mixer.stop()

        if justFinishedPreviousLevel:
            self.engine.say('Congratulations! You have finished level '+(str(self.level-1) if self.level>1 else '6')+'. Your current score is: '+str(self.score)+' You have '+str(self.lives)+' lives left.')
            self.engine.runAndWait()
            
        self.pacman_radius=int((min(WIDTH, HEIGHT)-2*WALL_THICKNESS)/(2.0*self.grid_size))
        self.just_moved=False
        
        self.xLocation=0+WALL_THICKNESS+self.pacman_radius+(self.cell_size-2*self.pacman_radius)/2+(self.grid_size/2)*self.cell_size
        self.yLocation=0+WALL_THICKNESS+self.pacman_radius+(self.cell_size-2*self.pacman_radius)/2+(self.grid_size/2)*self.cell_size
        self.rotation=0
        self.left_ear_vector=(0, -1)
        self.right_ear_vector=(0, 1)
        
        self.fruit_information=[]
        fruit_locations=[]
        self.ghost_information=[]
        ghost_locations=[]

        #Initialize fruit location to be on top of pacman so loop finding its location will always run
        next_fruit_xLocation=self.grid_size/2
        next_fruit_yLocation=self.grid_size/2

        #Place fruits and any potential ghosts
        for fruit_index in xrange(0, number_of_fruits[self.level-1]):
            while (next_fruit_xLocation, next_fruit_yLocation)==(self.grid_size/2, self.grid_size/2) or (next_fruit_xLocation, next_fruit_yLocation) in fruit_locations: 
                next_fruit_xLocation=random.randint(0, self.grid_size-1)
                next_fruit_yLocation=random.randint(0, self.grid_size-1)
            self.fruit_information.append((next_fruit_xLocation, next_fruit_yLocation, pygame.image.load("images/"+fruit_image_filenames[fruit_index if fruit_index<len(fruit_image_filenames) else random.randint(0, len(fruit_image_filenames)-1)]).convert_alpha()))
            fruit_locations.append((next_fruit_xLocation, next_fruit_yLocation))

            pacman_fruit_distance=self.distance((next_fruit_xLocation, next_fruit_yLocation), (self.grid_size/2, self.grid_size/2))
            pacman_fruit_vector=((0+WALL_THICKNESS+self.cell_size*next_fruit_xLocation)-self.xLocation+self.pacman_radius, (0+WALL_THICKNESS+self.cell_size*next_fruit_yLocation)-self.yLocation+self.pacman_radius)
            pacman_fruit_vector=self.normalize(pacman_fruit_vector)
            self.init_fruit_sound(fruit_sound_files[fruit_index], 0.5/pacman_fruit_distance*(self.dot_product(pacman_fruit_vector, self.left_ear_vector)+1), 0.5/pacman_fruit_distance*(self.dot_product(pacman_fruit_vector, self.right_ear_vector)+1))
    
        if self.level>=4:
            next_ghost_xLocation=self.grid_size/2
            next_ghost_yLocation=self.grid_size/2
            for ghost_index in xrange(0, (self.level-4)/2+1):
                while ((next_ghost_xLocation, next_ghost_yLocation)==(self.grid_size/2, self.grid_size/2)) or ((next_ghost_xLocation, next_ghost_yLocation) in fruit_locations) or ((next_ghost_xLocation, next_ghost_yLocation) in ghost_locations):
                    next_ghost_xLocation=random.randint(0, self.grid_size-1)
                    next_ghost_yLocation=random.randint(0, self.grid_size-1)
                self.ghost_information.append((next_ghost_xLocation, next_ghost_yLocation, pygame.image.load("images/"+ghost_image_filenames[ghost_index]).convert_alpha()))
                ghost_locations.append((next_ghost_xLocation, next_ghost_yLocation))

                pacman_ghost_distance=self.distance((next_ghost_xLocation, next_ghost_yLocation), (self.grid_size/2, self.grid_size/2))
                pacman_ghost_vector=((0+WALL_THICKNESS+self.cell_size*next_ghost_xLocation)-self.xLocation+self.pacman_radius, (0+WALL_THICKNESS+self.cell_size*next_ghost_yLocation)-self.yLocation+self.pacman_radius)
                pacman_ghost_vector=self.normalize(pacman_ghost_vector)
                self.init_ghost_sound(ghost_sound_files[ghost_index], 0.5/pacman_ghost_distance*(self.dot_product(pacman_ghost_vector, self.left_ear_vector)+1), 0.5/pacman_ghost_distance*(self.dot_product(pacman_ghost_vector, self.right_ear_vector)+1))

    def init_fruit_sound(self, sound_file, left, right):
        sound=pygame.mixer.Sound("sounds/"+sound_file)
        channel=pygame.mixer.find_channel()
        channel.play(sound, loops=-1)
        if channel is not None:
            channel.set_volume(left, right)
        self.fruit_sound_channels.append(channel)

    def init_ghost_sound(self, sound_file, left, right):
        sound=pygame.mixer.Sound("sounds/"+sound_file)
        channel=pygame.mixer.find_channel()
        channel.play(sound, loops=-1)
        if channel is not None:
            channel.set_volume(left, right)
        self.ghost_sound_channels.append(channel)

    def adjust_fruit_sound(self, channel_index, left, right):
        if self.fruit_sound_channels[channel_index] is not None:
            self.fruit_sound_channels[channel_index].set_volume(left, right)

    def adjust_ghost_sound(self, channel_index, left, right):
        if self.ghost_sound_channels[channel_index] is not None:
            self.ghost_sound_channels[channel_index].set_volume(left, right)

    def stop_background_sounds(self):
        for channel in self.fruit_sound_channels+self.ghost_sound_channels:
            channel.stop()
            
    def silence_all_sounds(self):
        for index in xrange(len(self.fruit_sound_channels)):
            if self.fruit_sound_channels[index] is not None:
                self.fruit_sound_channels[index].set_volume(0, 0)
        for index in xrange(len(self.ghost_sound_channels)):
            if self.ghost_sound_channels[index] is not None:
                self.ghost_sound_channels[index].set_volume(0, 0)    
            
    def distance(self, point1, point2):
        return math.sqrt((point2[0]-point1[0])**2 + (point2[1]-point1[1])**2)
            
    def normalize(self, vector):
        length=math.sqrt(vector[0]**2 + vector[1]**2)
        return (vector[0]/float(length), vector[1]/float(length))

    def dot_product(self, vector1, vector2):
        return vector1[0]*vector2[0]+vector1[1]*vector2[1]

    def adjust_fruit_and_ghost_sounds(self):
        pacman_xLocation=int((self.xLocation-(self.cell_size-2*self.pacman_radius)/2-self.pacman_radius)/self.cell_size)
        pacman_yLocation=int((self.yLocation-(self.cell_size-2*self.pacman_radius)/2-self.pacman_radius)/self.cell_size)
        for fruit_index in xrange(len(self.fruit_information)):
            current_fruit_location=(self.fruit_information[fruit_index][0], self.fruit_information[fruit_index][1])
            pacman_fruit_distance=self.distance((current_fruit_location[0], current_fruit_location[1]), (pacman_xLocation, pacman_yLocation))
            pacman_fruit_vector=((0+WALL_THICKNESS+self.cell_size*current_fruit_location[0])-self.xLocation+self.pacman_radius, (0+WALL_THICKNESS+self.cell_size*current_fruit_location[1])-self.yLocation+self.pacman_radius)
            pacman_fruit_vector=self.normalize(pacman_fruit_vector)
            self.adjust_fruit_sound(fruit_index, 0.5/pacman_fruit_distance*(self.dot_product(pacman_fruit_vector, self.left_ear_vector)+1), 0.5/pacman_fruit_distance*(self.dot_product(pacman_fruit_vector, self.right_ear_vector)+1))
        for ghost_index in xrange(len(self.ghost_information)):
            current_ghost_location=(self.ghost_information[ghost_index][0], self.ghost_information[ghost_index][1])
            pacman_ghost_distance=self.distance((current_ghost_location[0], current_ghost_location[1]), (pacman_xLocation, pacman_yLocation))
            pacman_ghost_vector=((0+WALL_THICKNESS+self.cell_size*current_ghost_location[0])-self.xLocation+self.pacman_radius, (0+WALL_THICKNESS+self.cell_size*current_ghost_location[1])-self.yLocation+self.pacman_radius)
            pacman_ghost_vector=self.normalize(pacman_ghost_vector)
            self.adjust_ghost_sound(ghost_index, 0.5/pacman_ghost_distance*(self.dot_product(pacman_ghost_vector, self.left_ear_vector)+1), 0.5/pacman_ghost_distance*(self.dot_product(pacman_ghost_vector, self.right_ear_vector)+1))
            
    def run_game(self):
        while True:
            self.just_moved=False
            for event in pygame.event.get():
                if event.type==QUIT:
                    exit()
                    
            pressed_keys=pygame.key.get_pressed()
            pygame.draw.rect(self.screen, (0, 0, 0), (0, 0, WIDTH, HEIGHT), 0) #Clear screen each time
            
            if not self.gameRunning:
                if pressed_keys[K_SPACE]:
                    self.level=1
                    self.score=0
                    self.lives=4
                    self.gameRunning=True
                    self.start_level()
                
                game_over_text=self.font.render("Game Over!", 0, (255, 255, 0))
                score_text=self.font.render("Score: "+str(self.score), 0, (0, 0, 255))
                self.screen.blit(game_over_text, (175, 230))
                self.screen.blit(score_text, (175, 300))
                pygame.display.update()
                
                if self.gameJustEnded:
                    self.engine.say('Game over! Your final score is: '+str(self.score))
                    self.engine.runAndWait()
                    self.gameJustEnded=False
                    
                continue

            if pressed_keys[K_q] and not self.key_held_down:
                self.pacman_visible=not self.pacman_visible
                self.key_held_down=True
                
            if(not pressed_keys[K_LEFT] and not pressed_keys[K_RIGHT] and not pressed_keys[K_UP] and not pressed_keys[K_DOWN] and not pressed_keys[K_q]):
                self.key_held_down=False
                   
            original_xLocation=self.xLocation
            original_yLocation=self.yLocation
            original_rotation=self.rotation
            
            if not self.key_held_down:
                if pressed_keys[K_LEFT]:
                    self.rotation=(self.rotation-90 if self.rotation!=0 else 270)
                    self.key_held_down=True
                elif pressed_keys[K_RIGHT]:
                    self.rotation=(self.rotation+90 if self.rotation!=270 else 0)
                    self.key_held_down=True
                elif pressed_keys[K_UP]: #Try to go forwards
                    if self.rotation==0:
                        if self.xLocation+self.cell_size<WIDTH-WALL_THICKNESS:
                            self.xLocation+=self.cell_size
                        else:
                            self.misc_sound_channel.play(self.bump_sound)
                            self.key_held_down=True
                    elif self.rotation==90:
                        if self.yLocation+self.cell_size<HEIGHT-WALL_THICKNESS:
                            self.yLocation+=self.cell_size
                        else:
                            self.misc_sound_channel.play(self.bump_sound)
                            self.key_held_down=True
                    elif self.rotation==180:
                        if self.xLocation-self.cell_size>0+WALL_THICKNESS:
                            self.xLocation-=self.cell_size
                        else:
                            self.misc_sound_channel.play(self.bump_sound)
                            self.key_held_down=True
                    elif self.rotation==270:
                        if self.yLocation-self.cell_size>0+WALL_THICKNESS:
                            self.yLocation-=self.cell_size
                        else:
                            self.misc_sound_channel.play(self.bump_sound)
                            self.key_held_down=True
                elif pressed_keys[K_DOWN]: #Try to go backwards
                    if self.rotation==0:
                        if self.xLocation-self.cell_size>0+WALL_THICKNESS:
                            self.xLocation-=self.cell_size
                        else:
                            self.misc_sound_channel.play(self.bump_sound)
                            self.key_held_down=True
                    elif self.rotation==90:
                        if self.yLocation-self.cell_size>0+WALL_THICKNESS:
                            self.yLocation-=self.cell_size
                        else:
                            self.misc_sound_channel.play(self.bump_sound)
                            self.key_held_down=True
                    elif self.rotation==180:
                        if self.xLocation+self.cell_size<WIDTH-WALL_THICKNESS:
                            self.xLocation+=self.cell_size
                        else:
                            self.misc_sound_channel.play(self.bump_sound)
                            self.key_held_down=True
                    elif self.rotation==270:
                        if self.yLocation+self.cell_size<HEIGHT-WALL_THICKNESS:
                            self.yLocation+=self.cell_size
                        else:
                            self.misc_sound_channel.play(self.bump_sound)
                            self.key_held_down=True
                        
                if self.xLocation!=original_xLocation or self.yLocation!=original_yLocation:
                    self.key_held_down=True
                    self.just_moved=True
                if self.rotation!=original_rotation:
                    if self.rotation==0:
                        self.left_ear_vector=(0, -1)
                        self.right_ear_vector=(0, 1)
                    elif self.rotation==90:
                        self.left_ear_vector=(1, 0)
                        self.right_ear_vector=(-1, 0)
                    elif self.rotation==180:
                        self.left_ear_vector=(0, 1)
                        self.right_ear_vector=(0, -1)
                    elif self.rotation==270:
                        self.left_ear_vector=(-1, 0)
                        self.right_ear_vector=(1, 0)
        
            pygame.draw.rect(self.screen, level_colors[self.level-1], self.leftWall, 0)
            pygame.draw.rect(self.screen, level_colors[self.level-1], self.topWall, 0)
            pygame.draw.rect(self.screen, level_colors[self.level-1], self.rightWall, 0)
            pygame.draw.rect(self.screen, level_colors[self.level-1], self.bottomWall, 0)

            #Draw the grid lines
            for x in xrange(0, self.grid_size-1):
                pygame.draw.line(self.screen, level_colors[self.level-1], (0+WALL_THICKNESS+self.cell_size*(x+1), HEIGHT-WALL_THICKNESS), ((0+WALL_THICKNESS+self.cell_size*(x+1), 0+WALL_THICKNESS)))
                pygame.draw.line(self.screen, level_colors[self.level-1], (0+WALL_THICKNESS, 0+WALL_THICKNESS+self.cell_size*(x+1)), (WIDTH-WALL_THICKNESS, 0+WALL_THICKNESS+self.cell_size*(x+1)))
                    
            #Draw any fruits on the screen (and get rid of any that are eaten)
            fruit_index=0
            while fruit_index<len(self.fruit_information):
                current_fruit_location=(self.fruit_information[fruit_index][0], self.fruit_information[fruit_index][1])
                resized_fruit_image=pygame.transform.scale(self.fruit_information[fruit_index][2], (int(self.cell_size)-1, int(self.cell_size)-1))
                self.screen.blit(resized_fruit_image, (0+WALL_THICKNESS+self.cell_size*current_fruit_location[0]+1, 0+WALL_THICKNESS+self.cell_size*current_fruit_location[1]+1))
                if (int((self.xLocation-(self.cell_size-2*self.pacman_radius)/2-self.pacman_radius)/self.cell_size), int((self.yLocation-(self.cell_size-2*self.pacman_radius)/2-self.pacman_radius)/self.cell_size))==current_fruit_location:
                    self.adjust_fruit_sound(fruit_index, 0, 0)
                    if self.fruit_sound_channels[fruit_index] is not None and len(self.fruit_information)>1:
                        self.misc_sound_channel.play(self.eating_sound)
                        self.fruit_sound_channels[fruit_index].stop()
                    del self.fruit_information[fruit_index]
                    del self.fruit_sound_channels[fruit_index]
                    self.score+=1
                    fruit_index-=1
                elif self.xLocation!=original_xLocation or self.yLocation!=original_yLocation or self.rotation!=original_rotation:
                    pacman_xLocation=int((self.xLocation-(self.cell_size-2*self.pacman_radius)/2-self.pacman_radius)/self.cell_size)
                    pacman_yLocation=int((self.yLocation-(self.cell_size-2*self.pacman_radius)/2-self.pacman_radius)/self.cell_size)
                    pacman_fruit_distance=self.distance((current_fruit_location[0], current_fruit_location[1]), (pacman_xLocation, pacman_yLocation))
                    pacman_fruit_vector=((0+WALL_THICKNESS+self.cell_size*current_fruit_location[0])-self.xLocation+self.pacman_radius, (0+WALL_THICKNESS+self.cell_size*current_fruit_location[1])-self.yLocation+self.pacman_radius)
                    pacman_fruit_vector=self.normalize(pacman_fruit_vector)
                    self.adjust_fruit_sound(fruit_index, 0.5/pacman_fruit_distance*(self.dot_product(pacman_fruit_vector, self.left_ear_vector)+1), 0.5/pacman_fruit_distance*(self.dot_product(pacman_fruit_vector, self.right_ear_vector)+1))
                fruit_index+=1
            
            if len(self.fruit_information)<1:
                self.silence_all_sounds()
                self.level=self.level%6+1
                self.start_level(True)
                continue
                    
            #Draw any ghosts on the screen
            for ghost_index in xrange(0, len(self.ghost_information)):
                current_ghost_location=(self.ghost_information[ghost_index][0], self.ghost_information[ghost_index][1])
                resized_ghost_image=pygame.transform.scale(self.ghost_information[ghost_index][2], (int(self.cell_size)-1, int(self.cell_size)-1))
                self.screen.blit(resized_ghost_image, (0+WALL_THICKNESS+self.cell_size*current_ghost_location[0]+1, 0+WALL_THICKNESS+self.cell_size*current_ghost_location[1]+1))
                if (int((self.xLocation-(self.cell_size-2*self.pacman_radius)/2-self.pacman_radius)/self.cell_size), int((self.yLocation-(self.cell_size-2*self.pacman_radius)/2-self.pacman_radius)/self.cell_size))==current_ghost_location:
                    self.lives-=1
                    self.xLocation=0+WALL_THICKNESS+self.pacman_radius+(self.cell_size-2*self.pacman_radius)/2+(self.grid_size/2)*self.cell_size
                    self.yLocation=0+WALL_THICKNESS+self.pacman_radius+(self.cell_size-2*self.pacman_radius)/2+(self.grid_size/2)*self.cell_size
                    self.rotation=0
                    self.left_ear_vector=(0, -1)
                    self.right_ear_vector=(0, 1)
                    self.just_moved=False
                    pacman_ghost_distance=self.distance((current_ghost_location[0], current_ghost_location[1]), (self.grid_size/2, self.grid_size/2))
                    pacman_ghost_vector=((0+WALL_THICKNESS+self.cell_size*current_ghost_location[0])-self.xLocation+self.pacman_radius, (0+WALL_THICKNESS+self.cell_size*current_ghost_location[1])-self.yLocation+self.pacman_radius)
                    pacman_ghost_vector=self.normalize(pacman_ghost_vector)
                    self.misc_sound_channel.play(self.hit_sound)
                    if self.lives>=1:
                        self.silence_all_sounds()
                        self.engine.say('Ouch! You hit a ghost!')
                        self.engine.say('Your current score is: '+str(self.score)+' You have '+str(self.lives)+(' lives ' if self.lives!=1 else ' life ') +'left.')
                        self.engine.runAndWait()
                    self.adjust_ghost_sound(ghost_index, 0.5/pacman_ghost_distance*(self.dot_product(pacman_ghost_vector, self.left_ear_vector)+1), 0.5/pacman_ghost_distance*(self.dot_product(pacman_ghost_vector, self.right_ear_vector)+1))
                    self.adjust_fruit_and_ghost_sounds()
                    break
                elif self.xLocation!=original_xLocation or self.yLocation!=original_yLocation or self.rotation!=original_rotation:
                    pacman_xLocation=int((self.xLocation-(self.cell_size-2*self.pacman_radius)/2-self.pacman_radius)/self.cell_size)
                    pacman_yLocation=int((self.yLocation-(self.cell_size-2*self.pacman_radius)/2-self.pacman_radius)/self.cell_size)
                    pacman_ghost_distance=self.distance((current_ghost_location[0], current_ghost_location[1]), (pacman_xLocation, pacman_yLocation))
                    pacman_ghost_vector=((0+WALL_THICKNESS+self.cell_size*current_ghost_location[0])-self.xLocation+self.pacman_radius, (0+WALL_THICKNESS+self.cell_size*current_ghost_location[1])-self.yLocation+self.pacman_radius)
                    pacman_ghost_vector=self.normalize(pacman_ghost_vector)
                    self.adjust_ghost_sound(ghost_index, 0.5/pacman_ghost_distance*(self.dot_product(pacman_ghost_vector, self.left_ear_vector)+1), 0.5/pacman_ghost_distance*(self.dot_product(pacman_ghost_vector, self.right_ear_vector)+1))

            if self.lives<1:
                self.silence_all_sounds()
                self.gameRunning=False
                self.gameJustEnded=True
                self.fruit_sound_channels=[]
                self.ghost_sound_channels=[]
                self.stop_background_sounds()
                    
            #Draw pacman himself if his visibility is turned on
            if(self.pacman_visible):
                pygame.gfxdraw.pie(self.screen, int(self.xLocation), int(self.yLocation), self.pacman_radius, (10 if self.just_moved else 30)+self.rotation, (350 if self.just_moved else 330)+self.rotation, (255, 0, 0))

            pygame.display.update()
            sleep(0.05)


if __name__=="__main__":
    game_object=GameObject()
    game_object.start_level()
    game_object.run_game()
