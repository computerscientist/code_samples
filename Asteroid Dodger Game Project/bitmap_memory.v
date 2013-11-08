`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date:    18:14:05 03/29/2012 
// Design Name: 
// Module Name:    bitmap_memory 
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
module bitmap_memory #(           // Observe new syntax for specifying parameters
   parameter Abits = 11,        // Number of bits in address
   parameter Dbits = 8         // Number of bits in data
)(
   input [Abits-1 : 0] addr,
   output [Dbits-1 : 0] dout
   );

	reg [Dbits-1 : 0] mem [1279 : 0];   // The actual registers where data is stored
	initial $readmemh("bitmap_memory.txt", mem, 0, 1279);
	
   assign dout = mem[addr];    // Memory read: read all the time, no clock involved
	
endmodule
