`timescale 1ns / 1ps

////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer:
//
// Create Date:   14:51:43 04/25/2012
// Design Name:   top
// Module Name:   C:/Users/dallara/Desktop/Rob/COMP_541_Labs/FinalProject/top_test.v
// Project Name:  FinalProject
// Target Device:  
// Tool versions:  
// Description: 
//
// Verilog Test Fixture created by ISE for module: top
//
// Dependencies:
// 
// Revision:
// Revision 0.01 - File Created
// Additional Comments:
// 
////////////////////////////////////////////////////////////////////////////////

module top_test;

	// Inputs
	reg clk;
	reg reset;
	reg ps2_clk;
	reg ps2_data;

	// Outputs
	wire [31:0] pc;
	wire [3:0] DispSelect;
	wire [7:0] SevenSegValue;
	wire [2:0] red;
	wire [2:0] green;
	wire [2:1] blue;
	wire hsync;
	wire vsync;

	// Instantiate the Unit Under Test (UUT)
	top uut (
		.clk(clk), 
		.reset(reset), 
		.ps2_clk(ps2_clk), 
		.ps2_data(ps2_data), 
		.pc(pc), 
		.DispSelect(DispSelect), 
		.SevenSegValue(SevenSegValue), 
		.red(red), 
		.green(green), 
		.blue(blue), 
		.hsync(hsync), 
		.vsync(vsync)
	);
	
	integer i;
	
	initial begin
		// Initialize Inputs
		clk = 0;
		reset = 0;
		ps2_clk = 0;
		ps2_data = 0;

		// Wait 200 ns for global reset to finish
		#200;
        
		// Add stimulus here
		for(i=0;i<13200;i=i+1)
			begin
				#30 clk=1;
				#30 clk=0;
			end
	end
      
endmodule

