`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date:    18:15:50 03/29/2012 
// Design Name: 
// Module Name:    screen_memory 
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
module screen_memory #(           // Observe new syntax for specifying parameters
   parameter Abits = 11,        // Number of bits in address
   parameter Dbits = 3         // Number of bits in data
)(
	input clock,
   input wr,
	input [Abits-1:0] screenAddr, //Address that display unit looks at
   input [Abits-1 : 0] addr, //Address that CPU looks at
	input [Dbits-1 : 0] din,
   output [Dbits-1 : 0] dout,
	output [Dbits-1 : 0] character
   );

	reg [Dbits-1 : 0] mem [1199 : 0];   // The actual registers where data is stored
	initial $readmemb("screen_memory.txt", mem, 0, 1199);
	
	always @(posedge clock)     // Memory write: only when wr==1, and only at posedge clock
      if(wr)
			mem[addr] <= din;
			
   assign dout = mem[addr];    // Memory read: read all the time, no clock involved
	assign character = mem[screenAddr];
	
endmodule
