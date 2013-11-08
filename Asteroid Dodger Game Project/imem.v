`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date:    02:05:08 04/12/2012 
// Design Name: 
// Module Name:    imem 
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
module imem #(           // Observe new syntax for specifying parameters
   parameter inputAbits = 32,   // Number of bits in input address (to fit processor diagram)
	parameter Abits = 9,			  // Number of bits in actual memory address
   parameter Dbits = 32         // Number of bits in data
)(
   input [inputAbits-1 : 0] addr,
   output [Dbits-1 : 0] dout //Read out 4-bit words
   );

	reg [Dbits-1 : 0] mem [(2**Abits)-1 : 0];   // The actual registers where data is stored
	initial $readmemb("asteroid_dodger.txt", mem, 0);
	
   assign dout = mem[addr[Abits-1:0]];    // Memory read: read all the time, no clock involved
	
endmodule
