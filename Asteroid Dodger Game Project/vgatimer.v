`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date:    15:05:03 02/24/2012 
// Design Name: 
// Module Name:    vgatimer 
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

module vgatimer(
	input clk,
	output hsync,
	output vsync,
	output activevideo,
	output [`xbits-1:0] x,
	output [`ybits-1:0] y
    );

	//These lines below allow you to count every 2nd clock tick and 4th clock tick
	//This is because, depending on the display mode, we may need to count at 100 MHz
	//or 50 MHz or 25MHz.
	
	reg [1:0] clk_count=0;
	always @(posedge clk)
		clk_count<=clk_count+2'b01;
	assign Every2ndTick=(clk_count[0]==1'b1);
	assign Every4thTick=(clk_count[1:0]==2'b11);

	//This part instantiates an xy-counter using the appropriate clock tick counter
	// xycounter xy(clk,         1'b1, x, y); //Count at 100 MHz
	// xycounter xy(clk, Every2ndTick, x, y); //Count at 50 MHz
	
		xycounter #(`xbits, `ybits, `WholeLine, `WholeFrame) xy(clk, Every4thTick, x, y); //Count at 25 MHz
		
		
	// Generate the monitor sync signals
	assign hsync=`hSyncPolarity^((x>=`hSyncStart && x<=`hSyncEnd) ? 1 : 0);
	assign vsync=`vSyncPolarity^((y>=`vSyncStart && y<=`vSyncEnd) ? 1 : 0);
	assign activevideo=(x>=0 && x<`hVisible && y>=0 && y<`vVisible) ? 1 : 0;

endmodule
