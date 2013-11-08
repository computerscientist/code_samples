`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date:    16:09:43 03/31/2012 
// Design Name: 
// Module Name:    ALU 
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
module ALU(
    input [31:0] A,
    input [31:0] B,
	 input Slt, //Set if less than (used for slt instructions)
    input Sub,
    input [1:0] Bool,
    input Shft,
    input Math,
    output V,
    output C,
    output N,
    output Z,
	 output [31:0] R
    ); //An ALU based on the design in L17 (Slides 20-27)
	
	wire Cin;
	wire subtract;
	
	wire [31:0] addSubResult;
	wire [31:0] BorNotB;
	wire [31:0] shiftResult;
	wire [31:0] R=0;
	
	shifter variableShifter(B, A[4:0], ~(Bool[1] | Bool[0]), ~(Bool[1] & Bool[0]), shiftResult);
	
	assign {C, addSubResult}=A + BorNotB + Cin;
	assign subtract=(Sub | Slt);
	assign BorNotB=subtract ? ~B : B;
	assign Cin=subtract ? 1 : 0;
	
	assign V=(A[31] & BorNotB[31] & ~R[31]) | (~A[31] & ~BorNotB[31] & R[31]);
	assign N=R[31];
	assign Z=~|R;
	
	assign R=(Slt ? {31'b0, addSubResult[31]} :
				(Shft==0 & Math==1) ? addSubResult :
			   (Shft==1 & Math==1) ? Bool[0] :
				(Shft==1 & Math==0) ? shiftResult :
				(Bool[1] ? (Bool[0] ? ~(A | B) : (A ^ B)) : (Bool[0] ? (A | B) : (A & B)))); //Last line deals with Shft==0 & Math==0
	
endmodule
