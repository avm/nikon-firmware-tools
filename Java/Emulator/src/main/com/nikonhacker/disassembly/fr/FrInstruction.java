package com.nikonhacker.disassembly.fr;


import com.nikonhacker.disassembly.Instruction;
import com.nikonhacker.disassembly.OutputOption;

import java.util.EnumSet;
import java.util.Set;

public class FrInstruction extends Instruction {

    /**
     * All 16bit variations of opcode and arguments
     */
    public static FrInstruction[] instructionMap = new FrInstruction[0x10000];


    /**
     * Instruction types (formats)
     */

    /** Layout of type A instructions is as follows : <pre>[   op          |  Rj   |  Ri   ]</pre> */
    public static final int FORMAT_A = 0;

    /** Layout of type B instructions is as follows : <pre>[   op  |       x       |  Ri   ]</pre> */
    public static final int FORMAT_B = 1;

    /** Layout of type C instructions is as follows : <pre>[   op          |   x   |  Ri   ]</pre> */
    public static final int FORMAT_C = 2;

    /** Layout of type D instructions is as follows : <pre>[   op          |       x       ]</pre> */
    public static final int FORMAT_D = 3;

    /** Layout of type E instructions is as follows : <pre>[   op                  |  Ri   ]</pre> */
    public static final int FORMAT_E = 4;

    /** Layout of type F instructions is as follows : <pre>[   op    |     offset / 2      ]</pre> */
    public static final int FORMAT_F = 5;

    /** Layout of type Z instructions is as follows : <pre>[   op                          ]</pre> */
    public static final int FORMAT_Z = 6;

    /** Layout for data reading is as follows :       <pre>[               x               ]</pre> */
    public static final int FORMAT_W = 7;


    /**
     * Main instruction map
     * These are the official names from Fujitsu's spec
     */
    private static final FrInstruction[] baseInstructions = {
/*      new FrInstruction( encod ,  mask , format  ,nX,nY, name,     displayFmt,     action     , Type,     isCond, delay) */
        new FrInstruction( 0x0000, 0xFF00, FORMAT_A, 0, 0, "LD",     "@(A&j),i",     "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x0100, 0xFF00, FORMAT_A, 0, 0, "LDUH",   "@(A&j),i",     "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x0200, 0xFF00, FORMAT_A, 0, 0, "LDUB",   "@(A&j),i",     "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x0300, 0xFF00, FORMAT_C, 0, 0, "LD",     "@(S&4u),i",    "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x0400, 0xFF00, FORMAT_A, 0, 0, "LD",     "@j,i;Ju",      "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x0500, 0xFF00, FORMAT_A, 0, 0, "LDUH",   "@j,i;Ju",      "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x0600, 0xFF00, FORMAT_A, 0, 0, "LDUB",   "@j,i;Ju",      "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x0700, 0xFFF0, FORMAT_E, 0, 0, "LD",     "@S+,i",        "iwSw"     , FlowType.NONE, false, false),
        new FrInstruction( 0x0710, 0xFFF0, FORMAT_E, 0, 0, "MOV",    "i,P",          "Pw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x0780, 0xFFFF, FORMAT_E, 0, 0, "LD",     "@S+,g",        "Sw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x0781, 0xFFFF, FORMAT_E, 0, 0, "LD",     "@S+,g",        "Sw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x0782, 0xFFFF, FORMAT_E, 0, 0, "LD",     "@S+,g",        "Sw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x0783, 0xFFFF, FORMAT_E, 0, 0, "LD",     "@S+,g",        "Sw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x0784, 0xFFFF, FORMAT_E, 0, 0, "LD",     "@S+,g",        "Sw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x0785, 0xFFFF, FORMAT_E, 0, 0, "LD",     "@S+,g",        "Sw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x0790, 0xFFFF, FORMAT_Z, 0, 0, "LD",     "@S+,P",        "Sw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x0800, 0xFF00, FORMAT_D, 0, 0, "DMOV",   "@4u,A",        "Aw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x0900, 0xFF00, FORMAT_D, 0, 0, "DMOVH",  "@2u,A",        "Aw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x0A00, 0xFF00, FORMAT_D, 0, 0, "DMOVB",  "@u,A",         "Aw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x0B00, 0xFF00, FORMAT_D, 0, 0, "DMOV",   "@4u,@-S",      "Sw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x0C00, 0xFF00, FORMAT_D, 0, 0, "DMOV",   "@4u,@A+",      "Aw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x0D00, 0xFF00, FORMAT_D, 0, 0, "DMOVH",  "@2u,@A+",      "Aw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x0E00, 0xFF00, FORMAT_D, 0, 0, "DMOVB",  "@u,@A+",       "Aw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x0F00, 0xFF00, FORMAT_D, 0, 0, "ENTER",  "#4u",          "SwFw"     , FlowType.NONE, false, false),
        new FrInstruction( 0x1000, 0xFF00, FORMAT_A, 0, 0, "ST",     "i,@(A&j)",     ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x1100, 0xFF00, FORMAT_A, 0, 0, "STH",    "i,@(A&j)",     ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x1200, 0xFF00, FORMAT_A, 0, 0, "STB",    "i,@(A&j)",     ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x1300, 0xFF00, FORMAT_C, 0, 0, "ST",     "i,@(S&4u)",    ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x1400, 0xFF00, FORMAT_A, 0, 0, "ST",     "i,@j;Ju",      ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x1500, 0xFF00, FORMAT_A, 0, 0, "STH",    "i,@j;Ju",      ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x1600, 0xFF00, FORMAT_A, 0, 0, "STB",    "i,@j;Ju",      ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x1700, 0xFFF0, FORMAT_E, 0, 0, "ST",     "i,@-S",        "Sw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x1710, 0xFFF0, FORMAT_E, 0, 0, "MOV",    "P,i",          "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x1780, 0xFFFF, FORMAT_E, 0, 0, "ST",     "g,@-S",        "Sw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x1781, 0xFFFF, FORMAT_E, 0, 0, "ST",     "g,@-S",        "Sw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x1782, 0xFFFF, FORMAT_E, 0, 0, "ST",     "g,@-S",        "Sw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x1783, 0xFFFF, FORMAT_E, 0, 0, "ST",     "g,@-S",        "Sw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x1784, 0xFFFF, FORMAT_E, 0, 0, "ST",     "g,@-S",        "Sw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x1785, 0xFFFF, FORMAT_E, 0, 0, "ST",     "g,@-S",        "Sw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x1790, 0xFFFF, FORMAT_Z, 0, 0, "ST",     "P,@-S",        "Sw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x1800, 0xFF00, FORMAT_D, 0, 0, "DMOV",   "A,@4u",        ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x1900, 0xFF00, FORMAT_D, 0, 0, "DMOVH",  "A,@2u",        ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x1A00, 0xFF00, FORMAT_D, 0, 0, "DMOVB",  "A,@u",         ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x1B00, 0xFF00, FORMAT_D, 0, 0, "DMOV",   "@S+,@4u",      "Sw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x1C00, 0xFF00, FORMAT_D, 0, 0, "DMOV",   "@A+,@4u",      "Aw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x1D00, 0xFF00, FORMAT_D, 0, 0, "DMOVH",  "@A+,@2u",      "Aw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x1E00, 0xFF00, FORMAT_D, 0, 0, "DMOVB",  "@A+,@u",       "Aw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x1F00, 0xFF00, FORMAT_D, 0, 0, "INT",    "#u",           "("        , FlowType.INT, false, false),
        new FrInstruction( 0x2000, 0xF000, FORMAT_B, 0, 0, "LD",     "@(F&4s),i",    "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x3000, 0xF000, FORMAT_B, 0, 0, "ST",     "i,@(F&4s)",    ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x4000, 0xF000, FORMAT_B, 0, 0, "LDUH",   "@(F&2s),i",    "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x5000, 0xF000, FORMAT_B, 0, 0, "STH",    "i,@(F&2s)",    ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x6000, 0xF000, FORMAT_B, 0, 0, "LDUB",   "@(F&s),i",     "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x7000, 0xF000, FORMAT_B, 0, 0, "STB",    "i,@(F&s)",     ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x8000, 0xFF00, FORMAT_C, 0, 0, "BANDL",  "#u,@i;Iu",     ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x8100, 0xFF00, FORMAT_C, 0, 0, "BANDH",  "#u,@i;Iu",     ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x8200, 0xFF00, FORMAT_A, 0, 0, "AND",    "j,i",          "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x8300, 0xFF00, FORMAT_D, 0, 0, "ANDCCR", "#u",           ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x8400, 0xFF00, FORMAT_A, 0, 0, "AND",    "j,@i;Iu",      ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x8500, 0xFF00, FORMAT_A, 0, 0, "ANDH",   "j,@i;Iu",      ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x8600, 0xFF00, FORMAT_A, 0, 0, "ANDB",   "j,@i;Iu",      ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x8700, 0xFF00, FORMAT_D, 0, 0, "STILM",  "#u",           ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x8800, 0xFF00, FORMAT_C, 0, 0, "BTSTL",  "#u,@i;Iu",     ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x8900, 0xFF00, FORMAT_C, 0, 0, "BTSTH",  "#u,@i;Iu",     ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x8A00, 0xFF00, FORMAT_A, 0, 0, "XCHB",   "@j,i;Ju",      "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x8B00, 0xFF00, FORMAT_A, 0, 0, "MOV",    "j,i",          "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x8C00, 0xFF00, FORMAT_D, 0, 0, "LDM0",   "z",            "Sw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x8D00, 0xFF00, FORMAT_D, 0, 0, "LDM1",   "y",            "Sw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x8E00, 0xFF00, FORMAT_D, 0, 0, "STM0",   "xz",           "Sw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x8F00, 0xFF00, FORMAT_D, 0, 0, "STM1",   "xy",           "Sw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x9000, 0xFF00, FORMAT_C, 0, 0, "BORL",   "#u,@i;Iu",     ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x9100, 0xFF00, FORMAT_C, 0, 0, "BORH",   "#u,@i;Iu",     ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x9200, 0xFF00, FORMAT_A, 0, 0, "OR",     "j,i",          "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x9300, 0xFF00, FORMAT_D, 0, 0, "ORCCR",  "#u",           ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x9400, 0xFF00, FORMAT_A, 0, 0, "OR",     "j,@i;Iu",      ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x9500, 0xFF00, FORMAT_A, 0, 0, "ORH",    "j,@i;Iu",      ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x9600, 0xFF00, FORMAT_A, 0, 0, "ORB",    "j,@i;Iu",      ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x9700, 0xFFF0, FORMAT_E, 0, 0, "JMP",    "@i;Iu",        "!"        , FlowType.JMP, false, false),
        new FrInstruction( 0x9710, 0xFFF0, FORMAT_E, 0, 0, "CALL",   "@i;Iu",        "("        , FlowType.CALL, false, false),
        new FrInstruction( 0x9720, 0xFFFF, FORMAT_Z, 0, 0, "RET",    "",             ")"        , FlowType.RET, false, false),
        new FrInstruction( 0x9730, 0xFFFF, FORMAT_Z, 0, 0, "RETI",   "",             ")"        , FlowType.RET, false, false),
        new FrInstruction( 0x9740, 0xFFF0, FORMAT_E, 0, 0, "DIV0S",  "i",            "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x9750, 0xFFF0, FORMAT_E, 0, 0, "DIV0U",  "i",            "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x9760, 0xFFF0, FORMAT_E, 0, 0, "DIV1",   "i",            "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x9770, 0xFFF0, FORMAT_E, 0, 0, "DIV2",   "i",            "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x9780, 0xFFF0, FORMAT_E, 0, 0, "EXTSB",  "i",            "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x9790, 0xFFF0, FORMAT_E, 0, 0, "EXTUB",  "i",            "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x97A0, 0xFFF0, FORMAT_E, 0, 0, "EXTSH",  "i",            "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x97B0, 0xFFF0, FORMAT_E, 0, 0, "EXTUH",  "i",            "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x97C0, 0xFFF0, FORMAT_E, 0, 0, "SRCH0",  "i",            "iw"       , FlowType.NONE, false, false), // FR80/FR81 only
        new FrInstruction( 0x97D0, 0xFFF0, FORMAT_E, 0, 0, "SRCH1",  "i",            "iw"       , FlowType.NONE, false, false), // FR80/FR81 only
        new FrInstruction( 0x97E0, 0xFFF0, FORMAT_E, 0, 0, "SRCHC",  "i",            "iw"       , FlowType.NONE, false, false), // FR80/FR81 only
        new FrInstruction( 0x9800, 0xFF00, FORMAT_C, 0, 0, "BEORL",  "#u,@i;Iu",     ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x9900, 0xFF00, FORMAT_C, 0, 0, "BEORH",  "#u,@i;Iu",     ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x9A00, 0xFF00, FORMAT_A, 0, 0, "EOR",    "j,i",          "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x9B00, 0xFF00, FORMAT_C, 1, 0, "LDI:20", "#u,i",         "iv"       , FlowType.NONE, false, false),
        new FrInstruction( 0x9C00, 0xFF00, FORMAT_A, 0, 0, "EOR",    "j,@i;Iu",      ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x9D00, 0xFF00, FORMAT_A, 0, 0, "EORH",   "j,@i;Iu",      ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x9E00, 0xFF00, FORMAT_A, 0, 0, "EORB",   "j,@i;Iu",      ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x9F00, 0xFFF0, FORMAT_E, 0, 0, "JMP:D",  "@i;Iu",        "_!"       , FlowType.JMP, false, true ),
        new FrInstruction( 0x9F10, 0xFFF0, FORMAT_E, 0, 0, "CALL:D", "@i;Iu",        "_("       , FlowType.CALL, false, true ),
        new FrInstruction( 0x9F20, 0xFFFF, FORMAT_Z, 0, 0, "RET:D",  "",             "_)"       , FlowType.RET, false, true ),
        new FrInstruction( 0x9F30, 0xFFFF, FORMAT_Z, 0, 0, "INTE",   "",             ""         , FlowType.INTE, false, false),
        new FrInstruction( 0x9F60, 0xFFFF, FORMAT_Z, 0, 0, "DIV3",   "",             ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x9F70, 0xFFFF, FORMAT_Z, 0, 0, "DIV4S",  "",             ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x9F80, 0xFFF0, FORMAT_E, 2, 0, "LDI:32", "#u,i",         "iv"       , FlowType.NONE, false, false),
        new FrInstruction( 0x9F90, 0xFFFF, FORMAT_Z, 0, 0, "LEAVE",  "",             ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x9FA0, 0xFFFF, FORMAT_Z, 0, 0, "NOP",    "",             ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x9FC0, 0xFFF0, FORMAT_E, 0, 1, "COPOP",  "#u,#c,l,k",    ""         , FlowType.NONE, false, false), // not in FR80/FR81
        new FrInstruction( 0x9FD0, 0xFFF0, FORMAT_E, 0, 1, "COPLD",  "#u,#c,j,k",    ""         , FlowType.NONE, false, false), // not in FR80/FR81
        new FrInstruction( 0x9FE0, 0xFFF0, FORMAT_E, 0, 1, "COPST",  "#u,#c,l,i",    "iw"       , FlowType.NONE, false, false), // not in FR80/FR81
        new FrInstruction( 0x9FF0, 0xFFF0, FORMAT_E, 0, 1, "COPSV",  "#u,#c,l,i",    "iw"       , FlowType.NONE, false, false), // not in FR80/FR81
        new FrInstruction( 0xA000, 0xFF00, FORMAT_C, 0, 0, "ADDN",   "#u,i",         "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xA100, 0xFF00, FORMAT_C, 0, 0, "ADDN2",  "#n,i",         "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xA200, 0xFF00, FORMAT_A, 0, 0, "ADDN",   "j,i",          "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xA300, 0xFF00, FORMAT_D, 0, 0, "ADDSP",  "#4s",          "Sw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xA400, 0xFF00, FORMAT_C, 0, 0, "ADD",    "#u,i",         "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xA500, 0xFF00, FORMAT_C, 0, 0, "ADD2",   "#n,i",         "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xA600, 0xFF00, FORMAT_A, 0, 0, "ADD",    "j,i",          "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xA700, 0xFF00, FORMAT_A, 0, 0, "ADDC",   "j,i",          "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xA800, 0xFF00, FORMAT_C, 0, 0, "CMP",    "#u,i",         "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xA900, 0xFF00, FORMAT_C, 0, 0, "CMP2",   "#n,i",         "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xAA00, 0xFF00, FORMAT_A, 0, 0, "CMP",    "j,i",          "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xAB00, 0xFF00, FORMAT_A, 0, 0, "MULU",   "j,i",          "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xAC00, 0xFF00, FORMAT_A, 0, 0, "SUB",    "j,i",          "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xAD00, 0xFF00, FORMAT_A, 0, 0, "SUBC",   "j,i",          "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xAE00, 0xFF00, FORMAT_A, 0, 0, "SUBN",   "j,i",          "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xAF00, 0xFF00, FORMAT_A, 0, 0, "MUL",    "j,i",          "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xB000, 0xFF00, FORMAT_C, 0, 0, "LSR",    "#d,i",         "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xB100, 0xFF00, FORMAT_C, 0, 0, "LSR2",   "#d,i",         "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xB200, 0xFF00, FORMAT_A, 0, 0, "LSR",    "j,i",          "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xB300, 0xFFF0, FORMAT_A, 0, 0, "MOV",    "i,h",          ""         , FlowType.NONE, false, false),
        new FrInstruction( 0xB310, 0xFFF0, FORMAT_A, 0, 0, "MOV",    "i,h",          ""         , FlowType.NONE, false, false),
        new FrInstruction( 0xB320, 0xFFF0, FORMAT_A, 0, 0, "MOV",    "i,h",          ""         , FlowType.NONE, false, false),
        new FrInstruction( 0xB330, 0xFFF0, FORMAT_A, 0, 0, "MOV",    "i,h",          ""         , FlowType.NONE, false, false),
        new FrInstruction( 0xB340, 0xFFF0, FORMAT_A, 0, 0, "MOV",    "i,h",          ""         , FlowType.NONE, false, false),
        new FrInstruction( 0xB350, 0xFFF0, FORMAT_A, 0, 0, "MOV",    "i,h",          ""         , FlowType.NONE, false, false),
        new FrInstruction( 0xB400, 0xFF00, FORMAT_C, 0, 0, "LSL",    "#d,i",         "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xB500, 0xFF00, FORMAT_C, 0, 0, "LSL2",   "#d,i",         "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xB600, 0xFF00, FORMAT_A, 0, 0, "LSL",    "j,i",          "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xB700, 0xFFF0, FORMAT_A, 0, 0, "MOV",    "h,i",          "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xB710, 0xFFF0, FORMAT_A, 0, 0, "MOV",    "h,i",          "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xB720, 0xFFF0, FORMAT_A, 0, 0, "MOV",    "h,i",          "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xB730, 0xFFF0, FORMAT_A, 0, 0, "MOV",    "h,i",          "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xB740, 0xFFF0, FORMAT_A, 0, 0, "MOV",    "h,i",          "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xB750, 0xFFF0, FORMAT_A, 0, 0, "MOV",    "h,i",          "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xB800, 0xFF00, FORMAT_C, 0, 0, "ASR",    "#d,i",         "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xB900, 0xFF00, FORMAT_C, 0, 0, "ASR2",   "#d,i",         "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xBA00, 0xFF00, FORMAT_A, 0, 0, "ASR",    "j,i",          "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xBB00, 0xFF00, FORMAT_A, 0, 0, "MULUH",  "j,i",          "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xBC00, 0xFF00, FORMAT_C, 0, 0, "LDRES",  "@i+,#u;Iu",    ""         , FlowType.NONE, false, false), // not in FR80/FR81
        new FrInstruction( 0xBD00, 0xFF00, FORMAT_C, 0, 0, "STRES",  "#u,@i+;Iu",    ""         , FlowType.NONE, false, false), // not in FR80/FR81
        new FrInstruction( 0xBF00, 0xFF00, FORMAT_A, 0, 0, "MULH",   "j,i",          "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xC000, 0xF000, FORMAT_B, 0, 0, "LDI:8",  "#u,i",         "iv"       , FlowType.NONE, false, false),
        new FrInstruction( 0xD000, 0xF800, FORMAT_F, 0, 0, "CALL",   "2ru",          "("        , FlowType.CALL, false, false),
        new FrInstruction( 0xD800, 0xF800, FORMAT_F, 0, 0, "CALL:D", "2ru",          "_("       , FlowType.CALL, false, true ),
        new FrInstruction( 0xE000, 0xFF00, FORMAT_D, 0, 0, "BRA",    "2ru",          "!"        , FlowType.JMP, false, false),
        new FrInstruction( 0xE100, 0xFF00, FORMAT_D, 0, 0, "BNO",    "2ru",          "?"        , FlowType.NONE, false, false),
        new FrInstruction( 0xE200, 0xFF00, FORMAT_D, 0, 0, "BEQ",    "2ru",          "?"        , FlowType.BRA, true , false),
        new FrInstruction( 0xE300, 0xFF00, FORMAT_D, 0, 0, "BNE",    "2ru",          "?"        , FlowType.BRA, true , false),
        new FrInstruction( 0xE400, 0xFF00, FORMAT_D, 0, 0, "BC",     "2ru",          "?"        , FlowType.BRA, true , false),
        new FrInstruction( 0xE500, 0xFF00, FORMAT_D, 0, 0, "BNC",    "2ru",          "?"        , FlowType.BRA, true , false),
        new FrInstruction( 0xE600, 0xFF00, FORMAT_D, 0, 0, "BN",     "2ru",          "?"        , FlowType.BRA, true , false),
        new FrInstruction( 0xE700, 0xFF00, FORMAT_D, 0, 0, "BP",     "2ru",          "?"        , FlowType.BRA, true , false),
        new FrInstruction( 0xE800, 0xFF00, FORMAT_D, 0, 0, "BV",     "2ru",          "?"        , FlowType.BRA, true , false),
        new FrInstruction( 0xE900, 0xFF00, FORMAT_D, 0, 0, "BNV",    "2ru",          "?"        , FlowType.BRA, true , false),
        new FrInstruction( 0xEA00, 0xFF00, FORMAT_D, 0, 0, "BLT",    "2ru",          "?"        , FlowType.BRA, true , false),
        new FrInstruction( 0xEB00, 0xFF00, FORMAT_D, 0, 0, "BGE",    "2ru",          "?"        , FlowType.BRA, true , false),
        new FrInstruction( 0xEC00, 0xFF00, FORMAT_D, 0, 0, "BLE",    "2ru",          "?"        , FlowType.BRA, true , false),
        new FrInstruction( 0xED00, 0xFF00, FORMAT_D, 0, 0, "BGT",    "2ru",          "?"        , FlowType.BRA, true , false),
        new FrInstruction( 0xEE00, 0xFF00, FORMAT_D, 0, 0, "BLS",    "2ru",          "?"        , FlowType.BRA, true , false),
        new FrInstruction( 0xEF00, 0xFF00, FORMAT_D, 0, 0, "BHI",    "2ru",          "?"        , FlowType.BRA, true , false),
        new FrInstruction( 0xF000, 0xFF00, FORMAT_D, 0, 0, "BRA:D",  "2ru",          "_!"       , FlowType.JMP, false, true ),
        new FrInstruction( 0xF100, 0xFF00, FORMAT_D, 0, 0, "BNO:D",  "2ru",          "_?"       , FlowType.NONE, false, true ),
        new FrInstruction( 0xF200, 0xFF00, FORMAT_D, 0, 0, "BEQ:D",  "2ru",          "_?"       , FlowType.BRA, true , true ),
        new FrInstruction( 0xF300, 0xFF00, FORMAT_D, 0, 0, "BNE:D",  "2ru",          "_?"       , FlowType.BRA, true , true ),
        new FrInstruction( 0xF400, 0xFF00, FORMAT_D, 0, 0, "BC:D",   "2ru",          "_?"       , FlowType.BRA, true , true ),
        new FrInstruction( 0xF500, 0xFF00, FORMAT_D, 0, 0, "BNC:D",  "2ru",          "_?"       , FlowType.BRA, true , true ),
        new FrInstruction( 0xF600, 0xFF00, FORMAT_D, 0, 0, "BN:D",   "2ru",          "_?"       , FlowType.BRA, true , true ),
        new FrInstruction( 0xF700, 0xFF00, FORMAT_D, 0, 0, "BP:D",   "2ru",          "_?"       , FlowType.BRA, true , true ),
        new FrInstruction( 0xF800, 0xFF00, FORMAT_D, 0, 0, "BV:D",   "2ru",          "_?"       , FlowType.BRA, true , true ),
        new FrInstruction( 0xF900, 0xFF00, FORMAT_D, 0, 0, "BNV:D",  "2ru",          "_?"       , FlowType.BRA, true , true ),
        new FrInstruction( 0xFA00, 0xFF00, FORMAT_D, 0, 0, "BLT:D",  "2ru",          "_?"       , FlowType.BRA, true , true ),
        new FrInstruction( 0xFB00, 0xFF00, FORMAT_D, 0, 0, "BGE:D",  "2ru",          "_?"       , FlowType.BRA, true , true ),
        new FrInstruction( 0xFC00, 0xFF00, FORMAT_D, 0, 0, "BLE:D",  "2ru",          "_?"       , FlowType.BRA, true , true ),
        new FrInstruction( 0xFD00, 0xFF00, FORMAT_D, 0, 0, "BGT:D",  "2ru",          "_?"       , FlowType.BRA, true , true ),
        new FrInstruction( 0xFE00, 0xFF00, FORMAT_D, 0, 0, "BLS:D",  "2ru",          "_?"       , FlowType.BRA, true , true ),
        new FrInstruction( 0xFF00, 0xFF00, FORMAT_D, 0, 0, "BHI:D",  "2ru",          "_?"       , FlowType.BRA, true , true ),
    };

    /**
     * This is a "catch-all" instruction used as a safety net for unknown instructions
     */
    static FrInstruction[] defaultInstruction = {
        new FrInstruction( 0x97FF, 0x0000, FORMAT_W, 0, 0, "UNK",    "",             ""         , FlowType.NONE, false, false),
    };


    /**
     * These are replacement names for all stack-related operations
     */
    static FrInstruction[] altStackInstructions = {
        new FrInstruction( 0x0700, 0xFFF0, FORMAT_E, 0, 0, "POP",    "i",            ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x0780, 0xFFFF, FORMAT_E, 0, 0, "POP",    "g",            ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x0781, 0xFFFF, FORMAT_E, 0, 0, "POP",    "g",            ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x0782, 0xFFFF, FORMAT_E, 0, 0, "POP",    "g",            ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x0783, 0xFFFF, FORMAT_E, 0, 0, "POP",    "g",            ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x0784, 0xFFFF, FORMAT_E, 0, 0, "POP",    "g",            ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x0785, 0xFFFF, FORMAT_E, 0, 0, "POP",    "g",            ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x0790, 0xFFFF, FORMAT_Z, 0, 0, "POP",    "P",            ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x0B00, 0xFF00, FORMAT_D, 0, 0, "PUSH",   "@4u",          ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x1700, 0xFFF0, FORMAT_E, 0, 0, "PUSH",   "i",            ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x1780, 0xFFFF, FORMAT_E, 0, 0, "PUSH",   "g",            ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x1781, 0xFFFF, FORMAT_E, 0, 0, "PUSH",   "g",            ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x1782, 0xFFFF, FORMAT_E, 0, 0, "PUSH",   "g",            ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x1783, 0xFFFF, FORMAT_E, 0, 0, "PUSH",   "g",            ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x1784, 0xFFFF, FORMAT_E, 0, 0, "PUSH",   "g",            ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x1785, 0xFFFF, FORMAT_E, 0, 0, "PUSH",   "g",            ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x1790, 0xFFFF, FORMAT_Z, 0, 0, "PUSH",   "P",            ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x1B00, 0xFF00, FORMAT_D, 0, 0, "POP",    "@u",           ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x8C00, 0xFF00, FORMAT_D, 0, 0, "POP",    "z",            ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x8D00, 0xFF00, FORMAT_D, 0, 0, "POP",    "y",            ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x8E00, 0xFF00, FORMAT_D, 0, 0, "PUSH",   "xz",           ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x8F00, 0xFF00, FORMAT_D, 0, 0, "PUSH",   "xy",           ""         , FlowType.NONE, false, false),
    };

    /**
     * These are replacement names for all "+16" shift opcodes (LSR2, LSL2, ASR2)
     */
    static FrInstruction[] altShiftInstructions = {
        new FrInstruction( 0xB100, 0xFF00, FORMAT_C, 0, 0, "LSR",    "#bd,i",        "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xB500, 0xFF00, FORMAT_C, 0, 0, "LSL",    "#bd,i",        "iw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xB900, 0xFF00, FORMAT_C, 0, 0, "ASR",    "#bd,i",        "iw"       , FlowType.NONE, false, false),
    };

    /**
     * These are replacement names for all some DMOV opcodes
     */
    static FrInstruction[] altDmovInstructions = {
        new FrInstruction( 0x0800, 0xFF00, FORMAT_D, 0, 0, "LD",     "@4u,A",        ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x0900, 0xFF00, FORMAT_D, 0, 0, "LDUH",   "@2u,A",        ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x0A00, 0xFF00, FORMAT_D, 0, 0, "LDUB",   "@u,A",         ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x1800, 0xFF00, FORMAT_D, 0, 0, "ST",     "A,@4u",        ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x1900, 0xFF00, FORMAT_D, 0, 0, "STUH",   "A,@2u",        ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x1A00, 0xFF00, FORMAT_D, 0, 0, "STUB",   "A,@u",         ""         , FlowType.NONE, false, false),
    };

    /**
     * These are replacement names for dedicated opcodes
     * working on ILM, CCR and SP so that they look the same as others
     */
    static FrInstruction[] altSpecialInstructions = {
        new FrInstruction( 0x8300, 0xFF00, FORMAT_D, 0, 0, "AND",    "#u,C",         "Cw"       , FlowType.NONE, false, false),
        new FrInstruction( 0x8700, 0xFF00, FORMAT_D, 0, 0, "MOV",    "#u,M",         ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x9300, 0xFF00, FORMAT_D, 0, 0, "OR",     "#u,C",         "Cw"       , FlowType.NONE, false, false),
        new FrInstruction( 0xA300, 0xFF00, FORMAT_D, 0, 0, "ADD",    "#4s,S",        ""         , FlowType.NONE, false, false),
    };

    /**
     * Fake OPCodes for data reading
     * Array index is a DataType.SpecType_XXX value
     */
    static FrInstruction[] opData = {
        new FrInstruction( 0x0000, 0x0000, FORMAT_W, 0, 0, "DW",     "u;a",         ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x0000, 0x0000, FORMAT_W, 1, 0, "DL",     "u;a",         ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x0000, 0x0000, FORMAT_W, 1, 0, "DL",     "u;a",         ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x0000, 0x0000, FORMAT_W, 1, 0, "DL",     "u;T #v",      ""         , FlowType.NONE, false, false),
        new FrInstruction( 0x0000, 0x0000, FORMAT_W, 1, 0, "DR",     "q;f",         ""         , FlowType.NONE, false, false),
    };


    public int encoding;
    public int mask;
    public int instructionFormat;
    public int numberExtraXWords;
    public int numberExtraYWords;
    public String name;
    public String displayFormat;
    public String action;


    /**
     * Default instruction decoding upon class loading
     */
    static {
        initOpcodeMap(EnumSet.noneOf(OutputOption.class));
    }

    /**
     * This method fills the instructionMap array with all possible variants of instruction word so that
     * OPCODE can be looked up by just getting instructionMap[instructionWord]
     */
    public static void initOpcodeMap(Set<OutputOption> options) {
        /* opcode decoding */
        // First, fill everything with a default dummy code as a safety net for unknown instructions
        expandOpCodes(instructionMap, defaultInstruction);
        // Then overwrite with actual instructions
        expandOpCodes(instructionMap, baseInstructions);
        // And optionally replace some opcodes with alternate versions
        if (options.contains(OutputOption.STACK))
            expandOpCodes(instructionMap, altStackInstructions);
        if (options.contains(OutputOption.SHIFT))
            expandOpCodes(instructionMap, altShiftInstructions);
        if (options.contains(OutputOption.DMOV))
            expandOpCodes(instructionMap, altDmovInstructions);
        if (options.contains(OutputOption.SPECIALS))
            expandOpCodes(instructionMap, altSpecialInstructions);
    }

    /**
     * This method iterates on the source array and "expands" each Instruction
     * so that all possible values of the variable parts (the 0 bits in the mask)
     * in the destination array point to this Instruction<br/>
     * e.g. if source is
     * <pre>new FrInstruction( 0x0000, 0xFF00, FORMAT_A, "LD",     "@(A&j),i",     "iw"        , null,   false, false, false, false, false)</pre>
     * then destination[0x00] to destination[0xFF] will all point to that object
     * @param destination the destination array to be filled
     * @param source the source array to be "expanded"
     */
    static void expandOpCodes(Instruction[] destination, FrInstruction[] source)
    {
        for (FrInstruction op : source)
        {
            int n = (~ op.mask) & 0xFFFF;
            for( int i = 0 ; i <= n ; i++)
            {
                destination[op.encoding | i] = op;
            }
        }
    }


    /**
     * Creates a new FrInstruction
     * @param encoding the value of the word instruction that matches this opcode if all parameters being 0
     * @param mask the part of the "encoding" that contains the actual instruction, the zeroes being used by operands
     * @param instructionFormat pattern that specifies how the instruction word should be split in parts
     * @param numberExtraXWords number of extra 16-bit words to be interpreted as x operand
     * @param numberExtraYWords number of extra 16-bit words to be interpreted as y operand (for coprocessor operations)
     * @param name the symbolic name
     * @param displayFormat a string specifying how to format operands. It is a list of characters among :<br/>
<pre>
2 : constant operand (x) must be multiplied by 2 (e.g. address of 16-bit data)<br/>
4 : constant operand (x) must be multiplied by 4 (e.g. address of 32-bit data)<br/>
r : constant operand (x) is a relative address<br/>
I : x is loaded from Ri if valid (0 otherwise) (?)<br/>
J : x is loaded from Rj if valid (0 otherwise) (?)<br/>
b : shift2 (?)<br/>
x : set bit #8 of x to indicate that register bitmap (x) used by this operation must be reversed
y : add 8 to c to mark that register bitmap (x) used by this operation represents R8-R15 and not R0-R7
<br/>
# ( ) + , - ; @ are copied as is<br/>
& : outputs a ,<br/>
<br/>
s : outputs x as a signed hex<br/>
u : outputs x as an unsigned hex<br/>
n : outputs x as a negative hex<br/>
d : outputs x as a decimal<br/>
a : outputs x as ASCII chars<br/>
f : outputs x as a float (hi-half as the dividend, low-half as the divider)<br/>
q : outputs x as a ratio : hi-half / low-half<br/>
p : outputs x as a pair of hex values : hi-half, lo-half<br/>
z : outputs x as a bitmap of register IDs (influenced by previous x and y chars)<br/>
<br/>
A : outputs "AC"<br/>
C : outputs "CCR"<br/>
F : outputs "FP"<br/>
M : outputs "ILM"<br/>
P : outputs "PS"<br/>
S : outputs "SP"<br/>
i : name of register pointed by i<br/>
j : name of register pointed by j<br/>
g : name of dedicated register pointed by i<br/>
h : name of dedicated register pointed by j<br/>
k : name of coprocessor register pointed by i<br/>
l : name of coprocessor register pointed by j<br/>
<br/>
v : outputs current PC value as a vector id (0xFF being the first of this memory area, going down to 0x00)
c : outputs coprocessor operation (c)<br/>
</pre>
     * @param action a string specifying how to interpret the instruction. It is a list of characters among :<br/>
* <pre>
'!': jump<br/>
'?': branch<br/>
'(': call<br/>
')': return<br/>
'_': instruction provides a delay slot<br/>
'A': current register is AC<br/>
'C': current register is CCR<br/>
'F': current register is FP<br/>
'P': current register is PS<br/>
'S': current register is SP<br/>
'i': current register is Ri<br/>
'j': current register is Rj<br/>
'w': current register is marked invalid<br/>
'v': current register is marked valid and loaded with the given value<br/>
'x': current register is undefined<br/>
     * @param flowType
     * @param isConditional
     * @param hasDelaySlot
     */
    public FrInstruction(int encoding, int mask, int instructionFormat, int numberExtraXWords, int numberExtraYWords, String name, String displayFormat, String action, FlowType flowType, boolean isConditional, boolean hasDelaySlot)
    {
        super(flowType, hasDelaySlot, isConditional);
        this.encoding = encoding;
        this.mask = mask;
        this.instructionFormat = instructionFormat;
        this.numberExtraXWords = numberExtraXWords;
        this.numberExtraYWords = numberExtraYWords;
        this.name = name;
        this.displayFormat = displayFormat;
        this.action = action;
    }

    @Override
    public String toString() {
        return name + "(0x" + Integer.toHexString(encoding) + ")";
    }
}
