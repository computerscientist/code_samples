`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date:    16:20:33 03/27/2012 
// Design Name: 
// Module Name:    displayunit 
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
module displayunit(
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
	
	wire [2:0] character;
	wire [7:0] colorValue;
	
	vgadisplaydriver displayDriver(clk, character, colorValue, red, green, blue, hsync, vsync, screenAddr, bitmapAddr);
	
endmodule
