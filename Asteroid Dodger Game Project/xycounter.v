`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date:    14:28:01 02/24/2012 
// Design Name: 
// Module Name:    xycounter 
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
module xycounter(
    input clk,
    input on,
    output reg [xbits-1:0] x=0,
    output reg [ybits-1:0] y=0
    );

	parameter xbits=2;
	parameter ybits=2;
	parameter width=4;
	parameter height=3;
	
	always @(posedge clk)
		if(on)
			if(x!=width-1)
				x<=x+1;
			else begin
				x<=0;
				if(y!=height-1)
					y<=y+1;
				else
					y<=0;
			end

endmodule
