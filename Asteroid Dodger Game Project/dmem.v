`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date:    02:13:11 04/12/2012 
// Design Name: 
// Module Name:    dmem 
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
module dmem #(           // Observe new syntax for specifying parameters
	parameter inputAbits = 32,  // Number of bits in input address (to fit processor diagram)
   parameter Abits = 12,        // Number of bits in actual memory address
   parameter Dbits = 32        // Number of bits in data
)(
   input clock,
   input wr,                   // WriteEnable:  if wr==1, then data is written into memory.
	input [15:0] keyboardChar,
   input [inputAbits-1 : 0] addr,   // Address for specifying memory location
   input [Dbits-1 : 0] din,    // Data for writing into memory (if wr==1)
   output [Dbits-1 : 0] dout   // Data read from memory (all the time)
   );
   
   reg [Dbits-1 : 0] mem [1615 : 0];   // The actual registers where data is stored
   //initial $readmemb("random.txt", mem, 64, 1615);
	
   always @(posedge clock)
      if(wr)
			mem[addr[Abits+1:2]] <= din;
   
   assign dout = (addr[Abits+1:2]==12'b000000111110) ? keyboardChar : mem[addr[Abits+1:2]];    // Memory read: read all the time, no clock involved
	
endmodule
