`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date:    02:23:52 04/12/2012 
// Design Name: 
// Module Name:    controller 
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
module controller(
    input [5:0] opcode,
	 input [5:0] funct,
	 input [3:0] flags,
	 output memtoreg,
	 output memwrite,
	 output pcsrc,
	 output alusrc,
	 output [1:0] regdst,
	 output regwrite,
	 output jump,
	 output [5:0] alucontrol,
	 output chooseShift,
	 output jal,
	 output jr
	 );
	
	wire [5:0] alucontrol;
	wire regwrite;
	wire [1:0] regdst;
	wire alusrc;
	wire pcsrc;
	wire memwrite;
	wire memtoreg;
	wire jump;
	wire chooseShift;
	wire jal;
	
	assign alucontrol={(~| opcode) & (funct==6'b101010),
							 ((~| opcode) & (funct==6'b100010)) | (opcode==6'b000100),
							 (~| opcode) & ((funct[5:1]==5'b00001) | (funct[5:1]==5'b10011)),
							 (~| opcode) & (((~| funct[5:2]) & (&funct[1:0])) | ((funct[5:2]==4'b1001) & funct[0])),
							 (~| opcode) & (~| funct[5:2]),
							 ((~| opcode) & ((funct[5:2]==4'b1000) | (funct[5:2]==4'b1010))) | (opcode==6'b100011) | (opcode==6'b101011) | (opcode==6'b000100) | (opcode==6'b001000)};
							 
	assign regwrite=((~| opcode) & ~(funct==6'b001000)) | opcode==6'b100011 | opcode==6'b001000 | opcode==6'b000011;
	
	assign regdst={(opcode==6'b000011),
						(~| opcode)};
						
	assign alusrc=~((~| opcode) | opcode==6'b000100);
	assign pcsrc=(opcode==6'b000100) & flags[0];
	assign memwrite=(opcode==6'b101011);
	assign memtoreg=(opcode==6'b100011);
	assign jump=(opcode==6'b000010) | (opcode==6'b000011);
	assign chooseShift=(~| opcode) & ((~| funct) | (funct==6'b000010) | (funct==6'b000011)); //
	assign jal=(opcode==6'b000011);
	assign jr=(~| opcode) & (funct==6'b001000);
	
endmodule
