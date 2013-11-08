`timescale 1ns / 1ps

// Montek Singh
// April 2012


module keyboard(
   input clk,
   input ps2_clk,
   input ps2_data,
   output reg [15:0] char = 0
   );

   reg [9:0] bits = 0;
   reg [3:0] count = 0;
   reg [1:0] ps2_clk_prev2 = 2'b11;
   reg [19:0] timeout = 0;
   
   always @(posedge clk)
      ps2_clk_prev2 <= {ps2_clk_prev2[0], ps2_clk};
      
   always @(posedge clk)
   begin
      if((count == 11) || (timeout[19] == 1))
      begin
         count <= 0;
         if((char[7:0] == 8'hE0) || (char[7:0] == 8'hF0))
            char[15:0] <= {char[7:0], bits[7:0]};
         else
            char[15:0] <= {8'b0, bits[7:0]};
      end
      else
      begin
         if(ps2_clk_prev2 == 2'b10)
         begin
            count <= count + 1;
            bits <= {ps2_data, bits[9:1]};
         end
      end
   end
   
   always @(posedge clk)
      timeout <= (count != 0) ? timeout + 1 : 0;

endmodule
