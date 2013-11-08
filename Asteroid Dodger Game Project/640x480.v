`define WholeLine  800   // x lies in [0..WholeLine-1]
`define WholeFrame 525   // y lies in [0..WholeFrame-1]

`define xbits 11   // how many bits are needed to count x?
`define ybits 11   // how many bits are needed to count y?
                   // 11 will work all the way up to a 2048x2048 display

`define hFrontPorch 16
`define hBackPorch 48
`define hSyncPulse 96
`define vFrontPorch 10
`define vBackPorch 33
`define vSyncPulse 2

`define hSyncPolarity 1'b1  // 1 means negative pulse!
`define vSyncPolarity 1'b1  // 1 means negative pulse!

`define hSyncStart (`WholeLine - `hBackPorch - `hSyncPulse)
`define hSyncEnd (`hSyncStart + `hSyncPulse - 1)
`define vSyncStart (`WholeFrame - `vBackPorch - `vSyncPulse)
`define vSyncEnd (`vSyncStart + `vSyncPulse - 1)

`define hVisible (`WholeLine  - `hFrontPorch - `hSyncPulse - `hBackPorch)
`define vVisible (`WholeFrame - `vFrontPorch - `vSyncPulse - `vBackPorch)
