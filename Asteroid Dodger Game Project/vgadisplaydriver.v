`timescale 1ns / 1ps

//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date:    16:07:28 02/24/2012 
// Design Name: 
// Module Name:    vgadisplaydriver 
// Project Name: 
// Target Devices: 
// Tool versions: 
// Description: 
//
// Dependencies: 
//
// Revision: 
// Revision 0.01 - File Created
// Additional Comments: 
//
//////////////////////////////////////////////////////////////////////////////////
`include "640x480.v"

module vgadisplaydriver(
    input clk,
	 input [2:0] character,
	 input [7:0] colorValue,
    output [2:0] red,
    output [2:0] green,
    output [2:1] blue,
    output hsync,
    output vsync,
	 output [10:0] screenAddr,
	 output [10:0] bitmapAddr
    );

   wire [10:0] x;
   wire [10:0] y;

   vgatimer myvgatimer(clk, hsync, vsync, activevideo, x, y);
   
	assign screenAddr = (x < `hVisible && y < `vVisible) ? ((`hVisible >> 4) * y[10:4] + x[10:4]) : 11'b0;
	assign bitmapAddr = (x < `hVisible && y < `vVisible) ? ({character, 8'b0} + {y[3:0], 4'b0} + x[3:0]) : 10'b0;
	
   assign red[2:0]   = (activevideo == 1) ? colorValue[7:5] : 3'b0;
   assign green[2:0] = (activevideo == 1) ? colorValue[4:2] : 3'b0;
   assign blue[2:1]  = (activevideo == 1) ? colorValue[1:0] : 2'b0;

endmodule

