`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date:    22:40:18 04/03/2012 
// Design Name: 
// Module Name:    shifter2 
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
module shifter(
   input [31:0] X,
   input [4:0] shamt,
   input LeftOrRight,    // 1 means left shift, 0 means right
   input LogOrArith,     // 1 means logical shift, 0 means arithmetic
   output [31:0] Result
   );

   wire [31:0] Y1, Y2, Y3, Y4, Y5;
   wire [31:0] Z1, Z2, Z3, Z4, Z5;
   
   assign Y1 = shamt[4] ? {X[15:0], 16'b0} : X;
   assign Y2 = shamt[3] ? {Y1[23:0], 8'b0} : Y1;
   assign Y3 = shamt[2] ? {Y2[27:0], 4'b0} : Y2;
   assign Y4 = shamt[1] ? {Y3[29:0], 2'b0} : Y3;
   assign Y5 = shamt[0] ? {Y4[30:0], 1'b0} : Y4;
   

   assign E = LogOrArith? 1'b0 : X[31];  // 1 means logical shift, 0 means arithmetic

   assign Z1 = shamt[4] ? {{16{E}}, X[31:16]} : X;
   assign Z2 = shamt[3] ? {{8{E}}, Z1[31:8]} : Z1;
   assign Z3 = shamt[2] ? {{4{E}}, Z2[31:4]} : Z2;
   assign Z4 = shamt[1] ? {{2{E}}, Z3[31:2]} : Z3;
   assign Z5 = shamt[0] ? {{1{E}}, Z4[31:1]} : Z4;
   
   assign Result = LeftOrRight ? Y5 : Z5;
   
endmodule
