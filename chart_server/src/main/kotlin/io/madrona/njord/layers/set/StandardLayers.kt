package io.madrona.njord.layers.set

import io.madrona.njord.layers.*


/**
 * airport / airfield (AIRARE)
 * anchor berth (ACHBRT)
 * anchorage area (ACHARE)
 * beacon cardinal (BCNCAR)
 * beacon isolated danger (BCNISD)
 * beacon lateral (BCNLAT)
 * beacon safe water (BCNSAW)
 * beacon special purpose/general (BCNSPP)
 * building single (BUISGL)
 * built-up area (BUAARE)
 * buoy cardinal (BOYCAR)
 * buoy installation (BOYINB)
 * buoy isolated danger (BOYISD)
 * buoy lateral (BOYLAT)
 * buoy safe water (BOYSAW)
 * buoy special purpose/general (BOYSPP)
 * cable area (CBLARE)
 * cable submarine (CBLSUB)
 * cargo transshipment area (CTSARE)
 * causeway (CAUSWY)
 * caution area (CTNARE)
 * crane (CRANES)
 * daymark (DAYMAR)
 * deep water route part (DWRTPT)
 * dredged area (DRGARE)
 * dumping ground (DMPGRD)
 * dyke (DYKCON)
 * fairway (FAIRWY)
 * fence/wall (FNCLNE)
 * ferry route (FERYRT)
 * ferry route (feryrt)
 * fishing ground (FSHGRD)
 * fog signal (FOGSIG)
 * fortified structure (FORSTC)
 * gate (GATCON)
 * hulk (HULKES)
 * incineration area (ICNARE)
 * inshore traffic zone (ISTZNE)
 * lake shore (LAKSHR)
 * land region (LNDRGN)
 * landmark (LNDMRK)
 * light (LIGHTS)
 * light float (LITFLT)
 * light vessel (LITVES)
 * marine farm/culture (MARCUL)
 * military practice area (MIPARE)
 * navigation line (NAVLNE)
 * offshore production area (OSPARE)
 * pile (PILPNT)
 * pilot boarding place (PILBOP)
 * pipeline area (PIPARE)
 * precautionary area (PRCARE)
 * production / storage area (PRDARE)
 * radar line (RADLNE)
 * radar range (RADRNG)
 * radar reflector (RADRFL)
 * radar transponder beacon (RTPBCN)
 * radio calling-in point (RDOCAL)
 * recommended route centerline (RCRTCL)
 * recommended track (RECTRC)
 * recommended traffic lane part (RCTLPT)
 * restricted area (RESARE)
 * retro-reflector (RETRFL)
 * river (RIVERS)
 * runway (RUNWAY)
 * sand waves (SNDWAV)
 * sea area / named water area (SEAARE)
 * sea-plane landing area (SPLARE)
 * signal station traffic (SISTAT)
 * signal station warning (SISTAW)
 * silo / tank (SILTNK)
 * sloping ground (SLOGRD)
 * submarine transit lane (SUBTLN)
 * swept area (SWPARE)
 * text ($TEXTS)
 * top mark (TOPMAR)
 * traffic separation line (TSELNE)
 * traffic separation scheme boundary (TSSBND)
 * traffic separation scheme crossing (TSSCRS)
 * traffic separation scheme lane part (TSSLPT)
 * traffic separation scheme roundabout (TSSRON)
 * traffic separation separation zone (TSEZNE)
 * tunnel (TUNNEL)
 * two-way route part (TWRTPT)
 * unsurveyed area (UNSARE)
 * new object (NEWOBJ)
 * navigational system of marks (M_NSYS)
 * cartographic symbol ($CSYMB)
 * text ($TEXTS)
 */
class StandardLayers {
    val layers = sequenceOf(
        Airare(),
        Achbrt(),
        Achare(),
        Bcncar(),
        Bcnisd(),
        Bcnlat(),
        Bcnsaw(),
        Bcnspp(),
        Buisgl(),
        Buaare(),
        Boycar(),
        Boyinb(),
        Boyisd(),
        Boylat(),
        Boysaw(),
        Boyspp(),
        Cblare(),
        Cblsub(),
        Ctsare(),
        Causwy(),
        Ctnare(),
        Cranes(),
        Daymar(),
        Dwrtpt(),
        Drgare(),
        Dmpgrd(),
        Dykcon(),
        Fairwy(),
        Fnclne(),
        Feryrt(),
//        feryrt(),
        Fshgrd(),
        Fogsig(),
        Forstc(),
        Icnare(),
        Istzne(),
        Lakshr(),
        Lndrgn(),
        Lndmrk(),
        Lights(),
        Litflt(),
        Litves(),
        Marcul(),
        Mipare(),
        Navlne(),
        Ospare(),
        Pilpnt(),
        Pilbop(),
        Pipare(),
        Prcare(),
        Prdare(),
        Radlne(),
        Radrng(),
        Radrfl(),
        Rtpbcn(),
        Rdocal(),
        Rcrtcl(),
        Rectrc(),
        Rctlpt(),
        Resare(),
        Retrfl(),
        Rivers(),
        Runway(),
        Sndwav(),
        Seaare(),
        Splare(),
        Sistat(),
        Sistaw(),
        Siltnk(),
        Slogrd(),
        Subtln(),
        Swpare(),
//        $texts(),
        Topmar(),
        Tselne(),
        Tssbnd(),
        Tsscrs(),
        Tsslpt(),
        Tssron(),
        Tsezne(),
        Tunnel(),
        Twrtpt(),
        Unsare(),
        Newobj(),
//        M_nsys(),
//    $csymb(),
//    $texts(),
    )
}
