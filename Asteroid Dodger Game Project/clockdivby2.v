`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date:    05:16:39 04/25/2012 
// Design Name: 
// Module Name:    clockdivby2 
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
module clockdivby2(input clk, output clkdiv2);

// Do not delete the following line; it is not a comment!
//synthesis attribute CLKDV_DIVIDE of dcm is 2
 
DCM_SP dcm (.CLKIN(clk), .CLKFB(clk0), .RST(1'b0), .PSEN(1'b0),
            .CLK0(clk0_dll), .CLK90(), .CLK180(), .CLK270(),
            .CLK2X(), .CLK2X180(), .CLKDV(clkdiv2), .LOCKED());

BUFG   clkg   (.I(clk0_dll), .O(clk0));
   
endmodule
