`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date:    02:35:05 04/12/2012 
// Design Name: 
// Module Name:    datapath 
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
module datapath(
	input clk,
	input reset,
	input memtoreg,
	input pcsrc,
   input alusrc,
	input [1:0] regdst,
	input regwrite, 
	input jump,
	input chooseShift,
	input jal,
	input jr,
   input [5:0] alucontrol,
   output [3:0] flags,
	output reg [31:0] pc=0,
	input [31:0] instr,
   output [31:0] aluout,
	output [31:0] writedata,
	input [31:0] readdata
    );
	
	wire [4:0] writereg;
	wire [31:0] result;
	wire [31:0] finalResult;
	wire [31:0] srcA;
	wire [31:0] srcB;
	
	wire [31:0] signimm;
	wire [31:0] newPC;
	
	wire [31:0] pcplus4;
	wire [31:0] pcbranch;
	
	wire [31:0] pcjump;
	wire [31:0] firstSource;
	
	register_file registerFile(clk, regwrite, instr[25:21], instr[20:16], writereg, finalResult, firstSource, writedata);
	ALU arithmeticLogicUnit(srcA, srcB, alucontrol[5], alucontrol[4], alucontrol[3:2], alucontrol[1], alucontrol[0], flags[3], flags[2], flags[1], flags[0], aluout);
	
	assign srcA=chooseShift ? instr[10:6] : firstSource;
	
	assign writereg=regdst[1] ? 5'b11111 : (regdst[0] ? instr[15:11] : instr[20:16]);
	assign result=memtoreg ? readdata : aluout;
	assign finalResult=jal ? pcplus4 : result;
	assign srcB=alusrc ? signimm : writedata;
	assign signimm={{16{instr[15]}}, instr[15:0]};
	
	assign newPC=jr ? firstSource : 
					 jump ? pcjump : 
					 pcsrc ? pcbranch : pcplus4;
					 
	assign pcplus4=pc+4;
	assign pcbranch=pcplus4+{signimm[29:0], 2'b0};
	assign pcjump={pcplus4[31:28], instr[25:0], 2'b0};
	
	always @(posedge clk)
		begin
			if(reset)
				pc<=0;
				
			else
				pc<=newPC;
		end
endmodule
