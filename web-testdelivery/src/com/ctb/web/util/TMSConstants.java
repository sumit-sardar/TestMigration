package com.ctb.web.util; 

import noNamespace.EntryType;
import noNamespace.StereotypeType;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.Manifest.Sco.ScoUnitType;
import noNamespace.TmssvcResponseDocument.TmssvcResponse.LoginResponse.Status.StatusCode;

public class TMSConstants 
{
        public static StatusCode.Enum getStatusCodeEnum(int value) {
        switch (value) {
            case 200: return StatusCode.X_200;
            case 400: return StatusCode.X_400;
            case 450: return StatusCode.X_450;
            case 451: return StatusCode.X_451;
            case 452: return StatusCode.X_452;
            case 461: return StatusCode.X_461;
            case 471: return StatusCode.X_471;
            case 472: return StatusCode.X_472;
            case 473: return StatusCode.X_473;
            case 474: return StatusCode.X_474;
            case 475: return StatusCode.X_475;
            case 476: return StatusCode.X_476;
            case 500: return StatusCode.X_500;
            case 501: return StatusCode.X_501;
            default: return StatusCode.X_501;
        }
    }
    
    public static EntryType.Enum getEntryEnum(String value) {
        if(value.equals("ab-inito")) return EntryType.AB_INITIO;
        if(value.equals("resume")) return EntryType.RESUME;
        else return EntryType.AB_INITIO;
    }
    
    public static ScoUnitType.Enum getScoUnitTypeEnum(String value) {
        if(value.equals("SUBTEST")) return ScoUnitType.SUBTEST;
        if(value.equals("SECTION")) return ScoUnitType.SECTION;
        else return ScoUnitType.SUBTEST;
    }
    
    public static StereotypeType.Enum getStereotypeEnum(String value) {
        if(value.equals("answerarea")) return StereotypeType.ANSWER_AREA;
        if(value.equals("directions")) return StereotypeType.DIRECTIONS;
        if(value.equals("stem")) return StereotypeType.STEM;
        if(value.equals("stimulus")) return StereotypeType.STIMULUS;
        else return null;
    }
} 
