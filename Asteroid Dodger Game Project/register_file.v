`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date:    15:50:40 03/31/2012 
// Design Name: 
// Module Name:    register_file 
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
module register_file(
	 input clock,
	 input RegWrite,
    input [4:0] ReadAddr1,
    input [4:0] ReadAddr2,
    input [4:0] WriteAddr,
    input [31:0] WriteData,
    output [31:0] ReadData1,
    output [31:0] ReadData2
    );

	reg [31:0] mem [31:0];   // The actual registers where data is stored
   
   always @(posedge clock)     // Register file write: only when wr==1, and only at posedge clock
      if(RegWrite)
         mem[WriteAddr] <= WriteData;
   
   assign ReadData1 = (ReadAddr1==5'b0) ? 0 : mem[ReadAddr1];
	assign ReadData2 = (ReadAddr2==5'b0) ? 0 : mem[ReadAddr2];	// Register file read: read all the time, no clock involved

endmodule
