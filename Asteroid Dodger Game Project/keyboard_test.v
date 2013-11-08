`timescale 1ns / 1ps

// Montek Singh
// April 2012

module keyboard_test(
	input clk,
	input ps2_data,
	input ps2_clk,
	output [15:0] char,
	output [7:0] SevenSegValue,
   output [3:0] DispSelect
   );
 
	keyboard keyb(clk, ps2_clk, ps2_data, char);
   
	display4digit disp(char, clk, SevenSegValue, DispSelect);

endmodule
