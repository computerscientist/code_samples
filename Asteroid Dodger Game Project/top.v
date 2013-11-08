`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date:    01:03:18 04/12/2012 
// Design Name: 
// Module Name:    top 
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
module top(
	input clk, 
	input reset,
	input ps2_clk,
	input ps2_data,
	output [31:0] pc,
	output [3:0] DispSelect,
	output [7:0] SevenSegValue,
	output [2:0] red,
   output [2:0] green,
   output [2:1] blue,
	output hsync,
	output vsync
   );

  wire [31:0] pc, instr, readdata, writedata, dataadr;
  wire [31:0] finalreaddata;
  
  wire [15:0] keyboardChar;
  wire [10:0] screenAddr;
  wire [10:0] bitmapAddr;
  wire [7:0] colorValue;
  wire [2:0] character;
  
  wire memwrite;
  wire clkdiv2;
  
  wire [11:0] cpuScreenMemoryInputAddr;
  assign cpuScreenMemoryInputAddr=dataadr[13:2]-12'b011001010000;
  
  mips mips(clkdiv2, reset, pc, instr, memwrite, dataadr,
            writedata, finalreaddata);		// processor

  imem imem(pc[10:2], instr);		// instr memory
  dmem dmem(clkdiv2, memwrite & dataadr[13:2]<12'b000001000000, keyboardChar, dataadr, writedata, readdata); // data memory
  
  screen_memory screenMemory(clkdiv2, memwrite & dataadr[13:2]>=12'b011001010000 & dataadr[13:2]<=12'b101011111111, screenAddr, cpuScreenMemoryInputAddr[10:0], writedata, screenMemReadData, character);
  bitmap_memory bitmapMemory(bitmapAddr, colorValue);
  
  keyboard keyBoard(clk, ps2_clk, ps2_data, keyboardChar);
  display4digit fourDigitDisplay(keyboardChar, clk, SevenSegValue, DispSelect);
  
  displayunit displayUnit(clk, character, colorValue, red, green, blue, hsync, vsync, screenAddr, bitmapAddr);
  clockdivby2 clockDivider(clk, clkdiv2);
  
  assign finalreaddata=(dataadr[13:2]<12'b011001010000) ? readdata :
							  (dataadr[13:2]<12'b101100000000) ? {29'b0, screenMemReadData} : 32'b0;
  
endmodule
