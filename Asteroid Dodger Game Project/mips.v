`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date:    02:18:47 04/12/2012 
// Design Name: 
// Module Name:    mips 
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
module mips(input         clk, reset,
            output [31:0] pc,
            input  [31:0] instr,
            output        memwrite,
            output [31:0] aluout, writedata,
            input  [31:0] readdata);

  wire        memtoreg, branch, pcsrc,
              alusrc, regwrite, jump,
				  chooseShift, jal, jr;
				  
  wire [5:0]  alucontrol;				// depends on your ALU
  wire [3:0]  flags;				// flags = {Z, V, C, N}
  wire [1:0] regdst;
  
  controller c(instr[31:26], instr[5:0], flags,
               memtoreg, memwrite, pcsrc,
               alusrc, regdst, regwrite, jump,
               alucontrol, chooseShift, jal, jr);
					
  datapath dp(clk, reset, memtoreg, pcsrc,
              alusrc, regdst, regwrite, jump,
              chooseShift, jal, jr, alucontrol,
              flags, pc, instr,
              aluout, writedata, readdata);
endmodule

