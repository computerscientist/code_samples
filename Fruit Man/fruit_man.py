import pygame
import pygame.gfxdraw
from pygame.locals import *
from sys import exit
import random
from time import sleep

WALL_THICKNESS=20
WIDTH=600
HEIGHT=600

fruit_image_filenames=['apple.png', 'bannana.png', 'cherry.png', 'orange.png', 'pair.png', 'watermelon.png']
ghost_image_filenames=['red_ghost.png', 'yellow_ghost.png']
number_of_fruits=[2, 3, 4, 5, 6, 8] #Number of fruits in different levels
grid_sizes=[8, 9, 10, 12, 14, 16]
level_colors=[(255, 0, 0), (0, 0, 255), (255, 255, 0), (0, 220, 220), (255, 165, 0), (0, 255, 0)]

class GameObject:
    def __init__(self):
        pygame.init()
        
        self.screen = pygame.display.set_mode((WIDTH, HEIGHT), 0, 32)
        self.font=pygame.font.SysFont(pygame.font.get_default_font(), 72)
        self.key_held_down=False
        
        self.leftWall=pygame.Rect(0, 0, WALL_THICKNESS, HEIGHT)
        self.topWall=pygame.Rect(0+WALL_THICKNESS, 0, WIDTH-WALL_THICKNESS, WALL_THICKNESS)
        self.rightWall=pygame.Rect(WIDTH-WALL_THICKNESS, 0+WALL_THICKNESS, WALL_THICKNESS, HEIGHT-WALL_THICKNESS)
        self.bottomWall=pygame.Rect(0+WALL_THICKNESS, HEIGHT-WALL_THICKNESS, WIDTH-2*WALL_THICKNESS, WALL_THICKNESS)

        self.level=1
        self.score=0
        self.lives=4
        self.gameRunning=True

    def start_level(self):
        self.grid_size=grid_sizes[self.level-1] #Width and height (in cells) of grid pacman runs around
        self.cell_size=(WIDTH-2.0*WALL_THICKNESS)/self.grid_size

        self.pacman_radius=int((min(WIDTH, HEIGHT)-2*WALL_THICKNESS)/(2.0*self.grid_size))
        self.just_moved=False
        
        self.xLocation=0+WALL_THICKNESS+self.pacman_radius+(self.cell_size-2*self.pacman_radius)/2+(self.grid_size/2)*self.cell_size
        self.yLocation=0+WALL_THICKNESS+self.pacman_radius+(self.cell_size-2*self.pacman_radius)/2+(self.grid_size/2)*self.cell_size
        self.rotation=0
        
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
            self.fruit_information.append((next_fruit_xLocation, next_fruit_yLocation, pygame.image.load(fruit_image_filenames[fruit_index if fruit_index<len(fruit_image_filenames) else random.randint(0, len(fruit_image_filenames)-1)]).convert_alpha()))
            fruit_locations.append((next_fruit_xLocation, next_fruit_yLocation))
    
        if self.level>=4:
            next_ghost_xLocation=self.grid_size/2
            next_ghost_yLocation=self.grid_size/2
            for ghost_index in xrange(0, (self.level-4)/2+1):
                while ((next_ghost_xLocation, next_ghost_yLocation)==(self.grid_size/2, self.grid_size/2)) or ((next_ghost_xLocation, next_ghost_yLocation) in fruit_locations) or ((next_ghost_xLocation, next_ghost_yLocation) in ghost_locations):
                    next_ghost_xLocation=random.randint(0, self.grid_size-1)
                    next_ghost_yLocation=random.randint(0, self.grid_size-1)
                self.ghost_information.append((next_ghost_xLocation, next_ghost_yLocation, pygame.image.load(ghost_image_filenames[ghost_index]).convert_alpha()))
                ghost_locations.append((next_ghost_xLocation, next_ghost_yLocation))
    
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
                continue
                    
            if(not pressed_keys[K_LEFT] and not pressed_keys[K_RIGHT] and not pressed_keys[K_UP] and not pressed_keys[K_DOWN]):
                self.key_held_down=False
                   
            original_xLocation=self.xLocation
            original_yLocation=self.yLocation
                
            if not self.key_held_down:
                if pressed_keys[K_LEFT] and self.xLocation-self.cell_size>0+WALL_THICKNESS:
                    self.xLocation-=self.cell_size
                    self.rotation=180
                elif pressed_keys[K_RIGHT] and self.xLocation+self.cell_size<WIDTH-WALL_THICKNESS:
                    self.xLocation+=self.cell_size
                    self.rotation=0
                elif pressed_keys[K_UP] and self.yLocation-self.cell_size>0+WALL_THICKNESS:
                    self.yLocation-=self.cell_size
                    self.rotation=270
                elif pressed_keys[K_DOWN] and self.yLocation+self.cell_size<HEIGHT-WALL_THICKNESS:
                    self.yLocation+=self.cell_size
                    self.rotation=90

                if self.xLocation!=original_xLocation or self.yLocation!=original_yLocation:
                    self.key_held_down=True
                    self.just_moved=True

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
                    del self.fruit_information[fruit_index]
                    self.score+=1
                    fruit_index-=1
                fruit_index+=1

            if len(self.fruit_information)<1:
                self.level=self.level%6+1
                self.start_level()
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
                    self.just_moved=False
                    break

            if self.lives<1:
                self.gameRunning=False
                    
            #Draw pacman himself
            pygame.gfxdraw.pie(self.screen, int(self.xLocation), int(self.yLocation), self.pacman_radius, (10 if self.just_moved else 30)+self.rotation, (350 if self.just_moved else 330)+self.rotation, (255, 0, 0))

            pygame.display.update()
            sleep(0.05)


if __name__=="__main__":
    game_object=GameObject()
    game_object.start_level()
    game_object.run_game()
    
