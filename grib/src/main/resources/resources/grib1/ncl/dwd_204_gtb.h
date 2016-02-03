/*
 * Deutscher Wetterdienst (DWD): Offenbach
 * Center: 78
 * Subcenter: 0
 * Parameter table version: 204
 */

TBLE2 dwd_204_params[] = {
{1,   "pressure RMS-error first guess - analysis",   "Pa", "P_RMS_FG_A"},
{2,   "pressure RMS-error initialised analysis - analysis",  "Pa", "P_RMS_IA_A"},
{3,   "u RMS-error first guess - analysis",  "m/s", "U_RMS_FG_A"},
{4,   "u RMS-error initialised analysis - analysis",     "m/s", "U_RMS_IA_A"},
{5,   "v RMS-error first guess - analysis",  "m/s", "V_RMS_FG_A"},
{6,   "v RMS-error initialised analysis - analysis",     "m/s", "V_RMS_IA_A"},
{7,   "geopotential RMS-error first guess - analysis",   "(m**2)/(s**2)", "FI_E_FG_A"},
{8,   "geopotential RMS-error init. analysis - analysis",    "(m**2)/(s**2)", "FI_E_IA_A"},
{9,   "relative humidity RMS-error first guess - analysis",  "%", "RH_E_FG_A"},
{10,  "rel. hum. RMS-error init. analysis - analysis",   "%", "RH_E_IA_A"},
{11,  "temperature RMS-error first guess - analysis",    "K", "T_RMS_FG_A"},
{12,  "temperature RMS-error init. analysis - analysis",     "K", "T_RMS_IA_A"},
{13,  "omega RMS-error first guess - analysis",  "m/s", "OM_E_FG_A"},
{14,  "omega RMS-error initialised analysis - analysis",     "m/s", "OM_E_IA_A"},
{15,  "kinetic energy RMS-error first guess - analysis",     "(m**2)/(s**2)", "E_FG_A_KE"},
{16,  "kinetic energy RMS-error init. analysis",     "(m**2)/(s**2)", "E_IG_A_KE"},
{21,  "wind speed analysis increment",    "m s-1", "FF_ANAI"},
{22,  "wind direction analysis increment",  "degree","DD_ANAI"},
{23,  "temperature analysis increment",   "K", "T_ANAI"},
{24,  "geopotential analysis increment",  "m2 s-2", "FI_ANAI"},
{25,  "pressure analysis increment",  "Pa", "P_ANAI"},
{26,  "msl pressure analysis increment",   "Pa", "PMSL_ANAI"},
{27,  "specific humidity analysis increment",     "kg kg-1", "QV_ANAI"},
{28,  "specific cloud water content analysis increment",  "kg kg-1", "QC_ANAI"},
{29,  "precipitable water analysis increment",    "kg m-2", "TQV_ANAI"},
{30,  "cloud water analysis increment",   "kg m-2", "TQC_ANAI"},
{90,  "probability of black ice",    "%", "BLACK_ICE"},
{91,  "probability of snowdrift",    "%", "SNOWDRIFT"},
{92,  "probability of strong snowdrift",     "%", "SSNOWDRIFT"},
{97,  "probability of thunderstorm",     "%", "THUNDERST"},
{98,  "probability of heavy thunderstorm",   "%", "HTHUNDERST"},
{99,  "probability of severe thunderstorm",  "%", "STHUNDERST"},
{101, "ensemble size",   "non-dim", "ENSEMBSIZE"},
{130, "probability of total precipitation > 10mm",    "%", "RR10"},
{131, "probability of total precipitation > 20mm",    "%", "RR20"},
{132, "probability of total precipitation > 25mm",    "%", "RR25"},
{133, "probability of total precipitation > 30mm",    "%", "RR30"},
{134, "probability of total precipitation > 40mm",    "%", "RR40"},
{135, "probability of total precipitation > 50mm",    "%", "RR50"},
{136, "probability of total precipitation > 60mm",    "%", "RR60"},
{137, "probability of total precipitation > 70mm",    "%", "RR70"},
{138, "probability of total precipitation > 80mm",    "%", "RR80"},
{139, "probability of total precipitation > 90mm",    "%", "RR90"},
{140, "probability of total precipitation > 100mm",   "%", "RR100"},
{141, "probability of maximum wind speed > 10m/s",    "%", "FF10"},
{142, "probability of maximum wind speed > 15m/s",    "%", "FF15"},
{143, "probability of maximum wind speed > 20m/s",    "%", "FF20"},
{144, "probability of maximum wind speed > 25m/s",    "%", "FF25"},
{151, "probability of accumulated snow > 1cm",    "%", "SNOW1"},
{152, "probability of accumulated snow > 5cm",    "%", "SNOW5"},
{153, "probability of accumulated snow > 10cm",   "%", "SNOW10"},
{154, "probability of accumulated snow > 15cm",   "%", "SNOW15"},
{155, "probability of accumulated snow > 20cm",   "%", "SNOW20"},
{156, "probability of accumulated snow > 25cm",   "%", "SNOW25"},
{161, "probability of maximum wind gust speed > 10m/s",   "%", "GUST10"},
{162, "probability of maximum wind gust speed > 14m/s",   "%", "GUST14"},
{163, "probability of maximum wind gust speed > 15m/s",   "%", "GUST15"},
{164, "probability of maximum wind gust speed > 18m/s",   "%", "GUST18"},
{165, "probability of maximum wind gust speed > 20m/s",   "%", "GUST20"},
{166, "probability of maximum wind gust speed > 25m/s",   "%", "GUST25"},
{167, "probability of maximum wind gust speed > 29m/s",   "%", "GUST29"},
{168, "probability of maximum wind gust speed > 33m/s",   "%", "GUST33"},
{169, "probability of maximum wind gust speed > 39m/s",   "%", "GUST39"},
{171, "probability of temperature < -10 degrees Celsius",  "%", "TT_LT_10"},
{172, "probability of temperature < 0 degrees Celsius",   "%", "TT_LT_0"},
{175, "probability of temperature > 25 degrees Celsius",  "%", "TT_GT_25"},
{176, "probability of temperature > 30 degrees Celsius",  "%", "TT_GT_30"},
{177, "probability of temperature > 35 degrees Celsius",  "%", "TT_GT_35"},
{181, "probability of MSL pressure < 980 hPa",    "%", "MSLP_LT_980"},
};
