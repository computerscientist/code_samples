`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date:    09:27:17 02/17/2012 
// Design Name: 
// Module Name:    display4digit 
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

module display4digit(
    input [15:0] A,
    input clk,
    output [7:0] segments,
    output [3:0] digitselect
    );

	reg [18:0] counter = 0;
	wire [1:0] toptwo;
	wire [3:0] value4bit;
	
	always @(posedge clk)
		counter <= counter + 1;
	
	assign toptwo[1:0] = counter[18:17];
	
	assign digitselect = ~ (  toptwo == 2'b00 ? 4'b0001  // Note inversion
									: toptwo == 2'b01 ? 4'b0010
									: toptwo == 2'b10 ? 4'b0100
															: 4'b1000 );
									
	assign value4bit   =   (  toptwo == 2'b00 ? A[3:0]
									: toptwo == 2'b01 ? A[7:4]
									: toptwo == 2'b10 ? A[11:8]
															: A[15:12] );
	
	HexTo7Seg myhexencoder(value4bit, segments);

endmodule


module HexTo7Seg(
    input [3:0] A,
    output [7:0] SevenSegValue
    );

	assign SevenSegValue = ~( A == 4'h0 ? 8'b11111100  // Note inversion
									: A == 4'h1 ? 8'b01100000
									: A == 4'h2 ? 8'b11011010
									: A == 4'h3 ? 8'b11110010
									: A == 4'h4 ? 8'b01100110
									: A == 4'h5 ? 8'b10110110
									: A == 4'h6 ? 8'b10111110
									: A == 4'h7 ? 8'b11100000
									: A == 4'h8 ? 8'b11111110
									: A == 4'h9 ? 8'b11110110
									: A == 4'hA ? 8'b11101110
									: A == 4'hB ? 8'b00111110
									: A == 4'hC ? 8'b10011100
									: A == 4'hD ? 8'b01111010
									: A == 4'hE ? 8'b10011110
													: 8'b10001110 );

endmodule
