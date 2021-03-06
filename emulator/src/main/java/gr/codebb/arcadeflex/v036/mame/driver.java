/*
This file is part of Arcadeflex.

Arcadeflex is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Arcadeflex is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Arcadeflex.  If not, see <http://www.gnu.org/licenses/>.
 */
package gr.codebb.arcadeflex.v036.mame;

//mame imports
import static arcadeflex.v037b7.mame.driverH.*;
//drivers imports
import static gr.codebb.arcadeflex.v036.drivers.pacman.*;
import static arcadeflex.v037b7.drivers.minivadr.*;
import static arcadeflex.v037b7.drivers.pengo.*;
import static arcadeflex.v037b7.drivers.ladybug.*;
import static gr.codebb.arcadeflex.v036.drivers.yamato.*;
import static arcadeflex.v037b7.drivers.bombjack.*;
import static gr.codebb.arcadeflex.v036.drivers._1942.*;
import static gr.codebb.arcadeflex.v036.drivers.system1.*;
import static gr.codebb.arcadeflex.v036.drivers.locomotn.*;
import static arcadeflex.v037b7.drivers.pooyan.*;
import static arcadeflex.v037b7.drivers.frogger.*;
import static arcadeflex.v037b7.drivers.bankp.*;
import static arcadeflex.v037b7.drivers.superqix.*;
import static gr.codebb.arcadeflex.v036.drivers.mrdo.*;
import static arcadeflex.v037b7.drivers.pingpong.*;
import static gr.codebb.arcadeflex.v036.drivers.tecmo.*;
import static arcadeflex.v037b7.drivers.rocnrope.*;
import static arcadeflex.v037b7.drivers.mikie.*;
import static arcadeflex.v037b7.drivers.hexa.*;
import static gr.codebb.arcadeflex.v036.drivers.sonson.*;
import static gr.codebb.arcadeflex.v036.drivers.amidar.*;
import static gr.codebb.arcadeflex.v036.drivers.bagman.*;
import static arcadeflex.v037b7.drivers.pbaction.*;
import static gr.codebb.arcadeflex.v036.drivers.kangaroo.*;
import static arcadeflex.v037b7.drivers.espial.*;
import static arcadeflex.v037b7.drivers.timeplt.*;
import static gr.codebb.arcadeflex.v036.drivers.gng.*;
import static gr.codebb.arcadeflex.v036.drivers.gundealr.*;
import static arcadeflex.v037b7.drivers.circusc.*;
import static arcadeflex.v037b7.drivers.tp84.*;
import static arcadeflex.v037b7.drivers.ironhors.*;
import static gr.codebb.arcadeflex.v036.drivers.gunsmoke.*;
import static gr.codebb.arcadeflex.v036.drivers.lwings.*;
import static gr.codebb.arcadeflex.v036.drivers.commando.*;
import static arcadeflex.v037b7.drivers.citycon.*;
import static arcadeflex.v037b7.drivers._1943.*;
import static gr.codebb.arcadeflex.v036.drivers.blktiger.*;
import static arcadeflex.v037b7.drivers.brkthru.*;
import static gr.codebb.arcadeflex.v036.drivers.sidearms.*;
import static arcadeflex.v037b7.drivers.retofinv.*;
import static gr.codebb.arcadeflex.v036.drivers.kyugo.*;
import static arcadeflex.v037b7.drivers.wc90.*;
import static arcadeflex.v037b7.drivers.jack.*;
import static gr.codebb.arcadeflex.v036.drivers.copsnrob.*;
import static arcadeflex.v037b7.drivers.pcktgal.*;
import static arcadeflex.v037b7.drivers.bogeyman.*;
import static arcadeflex.v037b7.drivers.scregg.*;
import static arcadeflex.v037b7.drivers.funkybee.*;
import static gr.codebb.arcadeflex.v036.drivers.xain.*;
import static gr.codebb.arcadeflex.v036.drivers.vulgus.*;
import static arcadeflex.v037b7.drivers.wiz.*;
import static arcadeflex.v037b7.drivers.shaolins.*;
import static gr.codebb.arcadeflex.v036.drivers.battlnts.*;
import static arcadeflex.v037b7.drivers.spy.*;
import static gr.codebb.arcadeflex.v036.drivers.superpac.*;
import static gr.codebb.arcadeflex.v036.drivers.mappy.*;
import static arcadeflex.v037b7.drivers.digdug.*;
import static arcadeflex.v037b7.drivers.phozon.*;
import static arcadeflex.v037b7.drivers.speedbal.*;
import static arcadeflex.v037b7.drivers.appoooh.*;
import static gr.codebb.arcadeflex.v036.drivers.scobra.*;
import static gr.codebb.arcadeflex.v036.drivers.scramble.*;
import static arcadeflex.v037b7.drivers.hcastle.*;
import static gr.codebb.arcadeflex.v036.drivers.exedexes.*;
import static arcadeflex.v037b7.drivers.pkunwar.*;
import static arcadeflex.v037b7.drivers.nova2001.*;
import static arcadeflex.v037b7.drivers.gsword.*;
import static arcadeflex.v037b7.drivers.stfight.*;
import static gr.codebb.arcadeflex.v037b7.drivers.jrpacman.*;
import static arcadeflex.v037b7.drivers.mpatrol.*;
import static arcadeflex.v037b7.drivers.yard.*;
import static gr.codebb.arcadeflex.v036.drivers.srumbler.*;
import static arcadeflex.v037b7.drivers.marineb.*;
import static arcadeflex.v037b7.drivers.jackal.*;
import static gr.codebb.arcadeflex.v036.drivers.cclimber.*;
import static arcadeflex.v037b7.drivers.gberet.*;
import static arcadeflex.v037b7.drivers.mitchell.*;
import static arcadeflex.v037b7.drivers.snk.*;
import static arcadeflex.v037b7.drivers.troangel.*;
import static gr.codebb.arcadeflex.v036.drivers.ddragon.*;
import static arcadeflex.v037b7.drivers.contra.*;
import static arcadeflex.v037b7.drivers.travrusa.*;
import static gr.codebb.arcadeflex.v036.drivers.sidepckt.*;
import static arcadeflex.v037b7.drivers.vigilant.*;
import static gr.codebb.arcadeflex.v036.drivers.dec8.*;
import static gr.codebb.arcadeflex.v036.drivers.m62.*;
import static gr.codebb.arcadeflex.v036.drivers.combatsc.*;
import static gr.codebb.arcadeflex.v036.drivers.trackfld.*;
import static arcadeflex.v037b7.drivers.jailbrek.*;
import static arcadeflex.v037b7.drivers.kingobox.*;
import static gr.codebb.arcadeflex.v036.drivers.atetris.*;
import static gr.codebb.arcadeflex.v036.drivers.mystston.*;
import static gr.codebb.arcadeflex.v036.drivers.btime.*;
import static gr.codebb.arcadeflex.v036.drivers.exprraid.*;
import static gr.codebb.arcadeflex.v036.drivers.matmania.*;
import static gr.codebb.arcadeflex.v036.drivers.renegade.*;
import static gr.codebb.arcadeflex.v036.drivers.hyperspt.*;
import static gr.codebb.arcadeflex.v036.drivers.flkatck.*;
import static gr.codebb.arcadeflex.v036.drivers.tnzs.*;
import static gr.codebb.arcadeflex.v036.drivers.airbustr.*;
import static gr.codebb.arcadeflex.v036.drivers.ddrible.*;
import static gr.codebb.arcadeflex.v036.drivers.lkage.*;
import static gr.codebb.arcadeflex.v036.drivers.mainevt.*;
import static arcadeflex.v037b7.drivers.bladestl.*;
import static arcadeflex.v037b7.drivers.rockrage.*;
import static gr.codebb.arcadeflex.v036.drivers.thunderx.*;
import static arcadeflex.v037b7.drivers.aliens.*;
import static arcadeflex.v037b7.drivers.crimfght.*;
import static gr.codebb.arcadeflex.v036.drivers.gbusters.*;
import static arcadeflex.v037b7.drivers.blockhl.*;
import static arcadeflex.v037b7.drivers.surpratk.*;
import static arcadeflex.v037b7.drivers.rollerg.*;
import static arcadeflex.v037b7.drivers._88games.*;
import static arcadeflex.v037b7.drivers.bottom9.*;
import static gr.codebb.arcadeflex.v036.drivers.cheekyms.*;
import static gr.codebb.arcadeflex.v036.drivers.slapfght.*;
import static gr.codebb.arcadeflex.v036.drivers.marvins.*;
import static gr.codebb.arcadeflex.v036.drivers.gladiatr.*;
import static gr.codebb.arcadeflex.v036.drivers.snowbros.*;
import static arcadeflex.v037b7.drivers.mnight.*;
import static gr.codebb.arcadeflex.v036.drivers.bublbobl.*;
import static arcadeflex.v037b7.drivers.zaxxon.*;
import static arcadeflex.v037b7.drivers.rallyx.*;
import static gr.codebb.arcadeflex.v036.drivers.z80bw.*;
import static gr.codebb.arcadeflex.v036.drivers.arkanoid.*;
import static arcadeflex.v037b7.drivers.congo.*;
import static arcadeflex.v037b7.drivers.skydiver.*;
import static arcadeflex.v037b7.drivers.simpsons.*;
import static arcadeflex.v037b7.drivers.vendetta.*;
import static arcadeflex.v037b7.drivers.missile.*;
import static arcadeflex.v037b7.drivers.yiear.*;
import static gr.codebb.arcadeflex.v036.drivers.pacland.*;
import static gr.codebb.arcadeflex.v036.drivers.skykid.*;
import static gr.codebb.arcadeflex.v036.drivers.baraduke.*;
import static gr.codebb.arcadeflex.v036.drivers.psychic5.*;
import static gr.codebb.arcadeflex.v036.drivers.wardner.*;
import static gr.codebb.arcadeflex.v036.drivers.galivan.*;
import static gr.codebb.arcadeflex.v036.drivers.tehkanwc.*;
import static arcadeflex.v037b7.drivers.solomon.*;
import static gr.codebb.arcadeflex.v036.drivers.sauro.*;
import static gr.codebb.arcadeflex.v036.drivers.shootout.*;
import static gr.codebb.arcadeflex.v036.drivers.ccastles.*;
import static gr.codebb.arcadeflex.v036.drivers.centiped.*;
import static gr.codebb.arcadeflex.v036.drivers.foodf.*;
import static gr.codebb.arcadeflex.v036.drivers.aeroboto.*;
import static arcadeflex.v037b7.drivers.goindol.*;
import static arcadeflex.v037b7.drivers.bjtwin.*;
import static gr.codebb.arcadeflex.v036.drivers.docastle.*;
import static gr.codebb.arcadeflex.v037b7.drivers.higemaru.*;
import static gr.codebb.arcadeflex.v036.drivers.fastfred.*;
import static gr.codebb.arcadeflex.v037b7.drivers.zodiack.*;
import static gr.codebb.arcadeflex.v036.drivers.taitosj.*;
import static arcadeflex.v037b7.drivers.blueprnt.*;
import static arcadeflex.v037b7.drivers.kchamp.*;
import static gr.codebb.arcadeflex.v036.drivers.qix.*;
import static gr.codebb.arcadeflex.v036.drivers.tutankhm.*;
import static gr.codebb.arcadeflex.v036.drivers.galaga.*;
import static gr.codebb.arcadeflex.v036.drivers.ajax.*;
import static gr.codebb.arcadeflex.v036.drivers.parodius.*;
import static gr.codebb.arcadeflex.v036.drivers.m72.*;
import static gr.codebb.arcadeflex.v036.drivers.raiden.*;
import static gr.codebb.arcadeflex.v036.drivers.dynduke.*;
import static gr.codebb.arcadeflex.v036.drivers.m92.*;
import static gr.codebb.arcadeflex.v036.drivers.sf1.*;
import static arcadeflex.v037b7.drivers.rastan.*;
import static gr.codebb.arcadeflex.v037b7.drivers.rainbow.*;
import static arcadeflex.v037b7.drivers.cabal.*;
import static gr.codebb.arcadeflex.v036.drivers.xmen.*;
import static gr.codebb.arcadeflex.v036.drivers.tmnt.*;
import static gr.codebb.arcadeflex.v036.drivers.toki.*;
import static gr.codebb.arcadeflex.v036.drivers.snk68.*;
import static gr.codebb.arcadeflex.v036.drivers.ddragon3.*;
import static gr.codebb.arcadeflex.v037b7.drivers.galpanic.*;
import static gr.codebb.arcadeflex.v036.drivers.tumblep.*;
import static gr.codebb.arcadeflex.v036.drivers.dec0.*;
import static gr.codebb.arcadeflex.v036.drivers.kaneko16.*;
import static gr.codebb.arcadeflex.v036.drivers.prehisle.*;
import static gr.codebb.arcadeflex.v036.drivers.cps1.*;
import static gr.codebb.arcadeflex.v036.drivers.alpha68k.*;
import static arcadeflex.v037b7.drivers.superman.*;
import static gr.codebb.arcadeflex.v036.drivers.aerofgt.*;
import static gr.codebb.arcadeflex.v036.drivers.neogeo.*;
import static gr.codebb.arcadeflex.v036.drivers.lastduel.*;
import static arcadeflex.v037b7.drivers.bloodbro.*;
import static gr.codebb.arcadeflex.v036.drivers.nemesis.*;
import static gr.codebb.arcadeflex.v036.drivers.sbasketb.*;
import static gr.codebb.arcadeflex.v036.drivers.terracre.*;
import static gr.codebb.arcadeflex.v036.drivers.punchout.*;
import static gr.codebb.arcadeflex.v036.drivers.astrof.*;
import static gr.codebb.arcadeflex.v036.drivers.karnov.*;
import static gr.codebb.arcadeflex.v036.drivers.shuuz.*;
import static arcadeflex.v037b7.drivers.berzerk.*;
import static gr.codebb.arcadeflex.v036.drivers.ssi.*;
import static gr.codebb.arcadeflex.v036.drivers.taitof2.*;
import static gr.codebb.arcadeflex.v036.drivers.galaxian.*;
import static gr.codebb.arcadeflex.v036.drivers.rockola.*;
import static gr.codebb.arcadeflex.v036.drivers.toaplan1.*;
import static gr.codebb.arcadeflex.v036.drivers.toaplan2.*;
import static gr.codebb.arcadeflex.v036.drivers.tigeroad.*;
import static gr.codebb.arcadeflex.v036.drivers.cninja.*;
import static gr.codebb.arcadeflex.v036.drivers.gaelco.*;
import static gr.codebb.arcadeflex.v036.drivers.gaiden.*;
import static gr.codebb.arcadeflex.v036.drivers.system16.*;
import static arcadeflex.v037b7.drivers.wc90b.*;

import static arcadeflex.v037b7.drivers.cop01.*;
import static gr.codebb.arcadeflex.v037b7.drivers.galspnbl.*;
import static gr.codebb.arcadeflex.v037b7.drivers.macross.*;

public class driver {

    public static GameDriver drivers[]
            = {
                /**
                 *
                 * Perfect games
                 */
                /*001*//*superpac*/driver_superpac,
                /*002*//*superpac*/ driver_superpcm,
                /*003*//*superpac*/ driver_pacnpal,
                /*004*//*superpac*/ driver_pacnchmp,
                /*005*//*pengo*/ driver_pengo,
                /*006*//*pengo*/ driver_pengo2,
                /*007*//*pengo*/ driver_pengo2u,
                /*008*//*pengo*/ driver_penta,
                /*009*//*mappy*/ driver_mappy,
                /*010*//*mappy*/ driver_mappyjp,
                /*011*//*mappy*/ driver_digdug2,
                /*012*//*mappy*/ driver_digdug2a,
                /*013*//*mappy*/ driver_todruaga,
                /*014*//*mappy*/ driver_todruagb,
                /*015*//*mappy*/ driver_motos,
                /*016*//*digdug*/ driver_digdug,
                /*017*//*digdug*/ driver_digdugb,
                /*018*//*digdug*/ driver_digdugat,
                /*019*//*digdug*/ driver_dzigzag,
                /*020*//*phozon*/ driver_phozon,
                /*021*//*ladybug*/ driver_ladybug,
                /*022*//*ladybug*/ driver_ladybugb,
                /*023*//*ladybug*/ driver_snapjack,
                /*024*//*ladybug*/ driver_cavenger,
                /*025*//*bankp*/ driver_bankp,
                /*026*//*pingpong*/ driver_pingpong,
                /*027*//*mikie*/ driver_mikie,
                /*028*//*mikie*/ driver_mikiej,
                /*029*//*mikie*/ driver_mikiehs,
                /*030*//*tp84*/ driver_tp84,
                /*031*//*tp84*/ driver_tp84a,
                /*032*//*bombjack*/ driver_bombjack,
                /*033*//*bombjac2*/ driver_bombjac2,
                /*034*//*pbaction*/ driver_pbaction,
                /*035*//*pbactio2*/ driver_pbactio2,
                /*036*//*pooyan*/ driver_pooyan,
                /*037*//*pooyan*/ driver_pooyans,
                /*038*//*pooyan*/ driver_pootan,
                /*039*//*rocnrope*/ driver_rocnrope,
                /*040*//*rocnrope*/ driver_rocnropk,
                /*041*//*locomotn*/ driver_jungler,
                /*042*//*locomotn*/ driver_junglers,
                /*043*//*locomotn*/ driver_locomotn,
                /*044*//*locomotn*/ driver_gutangtn,
                /*045*//*locomotn*/ driver_cottong,
                /*046*//*locomotn*/ driver_commsega,
                /*047*//*funkybee*/ driver_funkybee,
                /*048*//*funkybee*/ driver_skylancr,
                /*049*//*bagman*/ driver_bagman,
                /*050*//*bagman*/ driver_bagnard,
                /*051*//*bagman*/ driver_bagmans,
                /*052*//*bagman*/ driver_bagmans2,
                /*053*//*bagman*/ driver_sbagman,
                /*054*//*bagman*/ driver_sbagmans,
                /*055*//*bagman*/ driver_pickin,
                /*056*//*frogger*/ driver_frogger,
                /*057*//*frogger*/ driver_frogseg1,
                /*058*//*frogger*/ driver_frogseg2,
                /*059*//*frogger*/ driver_froggrmc,
                /*060*//*hexa*/ driver_hexa,
                /*061*//*vulgus*/ driver_vulgus,
                /*062*//*vulgus*/ driver_vulgus2,
                /*063*//*vulgus*/ driver_vulgusj,
                /*064*//*sonson*/ driver_sonson,
                /*065*//*_1942*/ driver_1942,
                /*066*//*_1942*/ driver_1942a,
                /*067*//*_1942*/ driver_1942b,
                /*068*//*superqix*/ driver_sqixbl,
                /*069*//*amidar*/ driver_amidar,
                /*070*//*amidar*/ driver_amidaru,
                /*071*//*amidar*/ driver_amidaro,
                /*072*//*amidar*/ driver_amigo,
                /*073*//*amidar*/ driver_turtles,
                /*074*//*amidar*/ driver_turpin,
                /*075*//*amidar*/ driver_600,
                /*076*//*kyugo*/ driver_gyrodine,
                /*077*//*kyugo*/ driver_sonofphx,
                /*078*//*kyugo*/ driver_repulse,
                /*079*//*kyugo*/ driver_99lstwar,
                /*080*//*kyugo*/ driver_99lstwra,
                /*081*//*kyugo*/ driver_flashgal,
                /*082*//*kyugo*/ driver_srdmissn,
                /*083*//*kyugo*/ driver_airwolf,
                /*084*//*kyugo*/ driver_skywolf,
                /*085*//*kyugo*/ driver_skywolf2,
                /*086*//*pacman*/ driver_pacman,
                /*087*//*pacman*/ driver_pacmanjp,
                /*088*//*pacman*/ driver_pacmanm,
                /*089*//*pacman*/ driver_npacmod,
                /*090*//*pacman*/ driver_pacmod,
                /*091*//*pacman*/ driver_hangly,
                /*092*//*pacman*/ driver_hangly2,
                /*093*//*pacman*/ driver_puckman,
                /*094*//*pacman*/ driver_pacheart,
                /*095*//*pacman*/ driver_piranha,
                /*096*//*pacman*/ driver_pacplus,
                /*097*//*pacman*/ driver_mspacman,
                /*098*//*pacman*/ driver_mspacatk,
                /*099*//*pacman*/ driver_pacgal,
                /*100*//*pacman*/ driver_maketrax,
                /*101*//*pacman*/ driver_crush,
                /*102*//*pacman*/ driver_crush2,
                /*103*//*pacman*/ driver_crush3,
                /*104*//*pacman*/ driver_mbrush,
                /*105*//*pacman*/ driver_paintrlr,
                /*106*//*pacman*/ driver_eyes,
                /*107*//*pacman*/ driver_eyes2,
                /*108*//*pacman*/ driver_mrtnt,
                /*109*//*pacman*/ driver_ponpoko,
                /*110*//*pacman*/ driver_ponpokov,
                /*111*//*pacman*/ driver_theglob,
                /*112*//*pacman*/ driver_beastf,
                /*113*//*pacman*/ driver_dremshpr,
                /*114*//*pacman*/ driver_vanvan,
                /*115*//*pacman*/ driver_vanvans,
                /*116*//*pacman*/ driver_alibaba,
                /*117*//*bogeyman*/ driver_bogeyman,
                /*118*//*espial*/ driver_espial,
                /*119*//*espial*/ driver_espiale,
                /*120*//*espial*/ driver_kangaroo,
                /*121*//*espial*/ driver_kangaroa,
                /*122*//*espial*/ driver_kangarob,
                /*123*//*wiz*/ driver_stinger,
                /*124*//*wiz*/ driver_scion,
                /*125*//*wiz*/ driver_scionc,
                /*126*//*wiz*/ driver_wiz,
                /*127*//*wiz*/ driver_wizt,
                /*128*//*appoooh*/ driver_appoooh,
                /*129*//*retofinv*/ driver_retofinv,
                /*130*//*retofinv*/ driver_retofin1,
                /*131*//*retofinv*/ driver_retofin2,
                /*132*//*scregg*/ driver_scregg,
                /*133*//*scregg*/ driver_eggs,
                /*134*//*yamato*/ driver_yamato,
                /*135*//*yamato*/ driver_yamato2,
                /* Sega System 1 / System 2 games */
                /*136*//*system1*/ driver_starjack,
                /*137*//*system1*/ driver_starjacs,
                /*138*//*system1*/ driver_regulus,
                /*139*//*system1*/ driver_regulusu,
                /*140*//*system1*/ driver_upndown,
                /*141*//*system1*/ driver_mrviking,
                /*142*//*system1*/ driver_mrvikinj,
                /*143*//*system1*/ driver_swat,
                /*144*//*system1*/ driver_flicky,
                /*145*//*system1*/ driver_flicky2,
                /*146*//*system1*/ driver_bullfgtj,
                /*147*//*system1*/ driver_pitfall2,
                /*148*//*system1*/ driver_pitfallu,
                /*149*//*system1*/ driver_seganinj,
                /*150*//*system1*/ driver_seganinu,
                /*151*//*system1*/ driver_nprinces,
                /*152*//*system1*/ driver_nprincsu,
                /*153*//*system1*/ driver_nprincsb,
                /*154*//*system1*/ driver_imsorry,
                /*155*//*system1*/ driver_imsorryj,
                /*156*//*system1*/ driver_teddybb,
                /*157*//*system1*/ driver_hvymetal,
                /*158*//*system1*/ driver_myhero,
                /*159*//*system1*/ driver_myheroj,
                /*160*//*system1*/ driver_myherok,
                /*161*//*system1*/ driver_chplftb,
                /*162*//*system1*/ driver_chplftbl,
                /*163*//*system1*/ driver_4dwarrio,
                /*164*//*system1*/ driver_wboy,
                /*165*//*system1*/ driver_wboy2,
                /*166*//*system1*/ driver_wboy4,
                /*167*//*system1*/ driver_wboyu,
                /*168*//*system1*/ driver_wboy4u,
                /*169*//*system1*/ driver_wbdeluxe,
                /*170*//*system1*/ driver_wbml,
                /*171*//*system1*/ driver_wbmlju,
                /*172*//*jack*/ driver_jack,
                /*173*//*jack*/ driver_jack2,
                /*174*//*jack*/ driver_jack3,
                /*175*//*jack*/ driver_treahunt,
                /*176*//*jack*/ driver_zzyzzyxx,
                /*177*//*jack*/ driver_zzyzzyx2,
                /*178*//*jack*/ driver_brix,
                /*179*//*jack*/ driver_freeze,
                /*180*//*jack*/ driver_sucasino,
                /*181*//*timeplt*/ driver_timeplt,
                /*182*//*timeplt*/ driver_timepltc,
                /*183*//*timeplt*/ driver_spaceplt,
                /*184*//*timeplt*/ driver_psurge,
                /*185*//*mrdo*/ driver_mrdo,
                /*186*//*mrdo*/ driver_mrdot,
                /*187*//*mrdo*/ driver_mrdofix,
                /*188*//*mrdo*/ driver_mrlo,
                /*189*//*mrdo*/ driver_mrdu,
                /*190*//*mrdo*/ driver_mrdoy,
                /*191*//*mrdo*/ driver_yankeedo,
                /*192*//*battlnts*/ driver_battlnts,
                /*193*//*battlnts*/ driver_battlntj,
                /*194*//*tecmo*/ driver_gemini,
                /*195*//*tecmo*/ driver_silkworm,
                /*196*//*tecmo*/ driver_silkwrm2,
                /*197*//*scobra*/ driver_scobra,
                /*198*//*scobra*/ driver_scobras,
                /*199*//*scobra*/ driver_scobrab,
                /*200*//*scobra*/ driver_stratgyx,
                /*201*//*scobra*/ driver_stratgys,
                /*202*//*scobra*/ driver_armorcar,
                /*203*//*scobra*/ driver_armorca2,
                /*204*//*scobra*/ driver_spdcoin,
                /*205*//*scobra*/ driver_tazmania,
                /*206*//*scobra*/ driver_tazmani2,
                /*207*//*scobra*/ driver_calipso,
                /*208*//*scobra*/ driver_anteater,
                /*209*//*scobra*/ driver_rescue,
                /*210*//*scobra*/ driver_minefld,
                /*211*//*scobra*/ driver_losttomb,
                /*212*//*scobra*/ driver_losttmbh,
                /*213*//*scobra*/ driver_superbon,
                /*214*//*scobra*/ driver_hustler,
                /*215*//*scobra*/ driver_billiard,
                /*216*//*scobra*/ driver_hustlerb,
                /*217*//*scramble*/ driver_scramble,
                /*218*//*scramble*/ driver_scrambls,
                /*219*//*scramble*/ driver_atlantis,
                /*220*//*scramble*/ driver_atlants2,
                /*221*//*scramble*/ driver_theend,
                /*222*//*scramble*/ driver_theends,
                /*223*//*scramble*/ driver_ckongs,
                /*224*//*scramble*/ driver_froggers,
                /*225*//*scramble*/ driver_amidars,
                /*226*//*scramble*/ driver_triplep,
                /*227*//*scramble*/ driver_knockout,
                /*228*//*scramble*/ driver_mariner,
                /*229*//*scramble*/ driver_hotshock,
                /*230*//*battlnts*/ driver_thehustl,
                /*231*//*battlnts*/ driver_thehustj,
                /*232*//*speedbal*/ driver_speedbal,
                /*233*//*hcastle*/ driver_hcastle,
                /*234*//*hcastle*/ driver_hcastlea,
                /*235*//*hcastle*/ driver_hcastlej,
                /*236*//*blktiger*/ driver_blktiger,
                /*237*//*blktiger*/ driver_bktigerb,
                /*238*//*blktiger*/ driver_blkdrgon,
                /*239*//*blktiger*/ driver_blkdrgnb,
                /*240*//*citycon*/ driver_citycon,
                /*241*//*citycon*/ driver_citycona,
                /*242*//*citycon*/ driver_cruisin,
                /*243*//*ironhors*/ driver_ironhors,
                /*244*//*ironhors*/ driver_dairesya,
                /*245*//*wc90*/ driver_wc90,
                /*246*//*gundealr*/ driver_gundealr,
                /*247*//*gundealr*/ driver_gundeala,
                /*248*//*gundealr*/ driver_yamyam,
                /*249*//*gundealr*/ driver_wiseguy,
                /*250*//*commando*/ driver_commando,
                /*251*//*commando*/ driver_commandu,
                /*252*//*commando*/ driver_commandj,
                /*253*//*commando*/ driver_spaceinv,
                /*254*//*brkthru*/ driver_brkthru,
                /*255*//*brkthru*/ driver_brkthruj,
                /*256*//*brkthru*/ driver_darwin,
                /*257*//*gunsmoke*/ driver_gunsmoke,
                /*258*//*gunsmoke*/ driver_gunsmrom,
                /*259*//*gunsmoke*/ driver_gunsmoka,
                /*260*//*gunsmoke*/ driver_gunsmokj,
                /*261*//*gng*/ driver_gng,
                /*262*//*gng*/ driver_gnga,
                /*263*//*gng*/ driver_gngt,
                /*264*//*gng*/ driver_makaimur,
                /*265*//*gng*/ driver_makaimuc,
                /*266*//*gng*/ driver_makaimug,
                /*267*//*gng*/ driver_diamond,
                /*268*//*_1943*/ driver_1943,
                /*269*//*_1943*/ driver_1943j,
                /*270*//*sidearms*/ driver_sidearms,
                /*271*//*sidearms*/ driver_sidearmr,
                /*272*//*sidearms*/ driver_sidearjp,
                /*273*//*sidearms*/ driver_turtship,
                /*274*//*pcktgal*/ driver_pcktgal,
                /*275*//*pcktgal*/ driver_pcktgalb,
                /*276*//*pcktgal*/ driver_pcktgal2,
                /*277*//*pcktgal*/ driver_spool3,
                /*278*//*pcktgal*/ driver_spool3i,
                /*279*//*minivadr*/ driver_minivadr,
                /*280*//*copsnrob*/ driver_copsnrob,
                /*281*//*lwings*/ driver_sectionz,
                /*282*//*lwings*/ driver_sctionza,
                /*283*//*lwings*/ driver_trojan,
                /*284*//*lwings*/ driver_trojanr,
                /*285*//*lwings*/ driver_trojanj,
                /*286*//*lwings*/ driver_lwings,
                /*287*//*lwings*/ driver_lwings2,
                /*288*//*lwings*/ driver_lwingsjp,
                /*289*//*snk*/ driver_tnk3,
                /*290*//*snk*/ driver_tnk3j,
                /*291*//*snk*/ driver_fitegolf,
                /*292*//*snk*/ driver_tdfever,
                /*293*//*snk*/ driver_tdfeverj,
                /*294*//*exedexes*/ driver_exedexes,
                /*295*//*exedexes*/ driver_savgbees,
                /*296*//*bublbobl*/ driver_boblbobl,
                /*297*//*bublbobl*/ driver_sboblbob,
                /*298*//*bublbobl*/ driver_tokiob,
                /*299*//*nova2001*/ driver_nova2001,
                /*300*//*nova2001*/ driver_nov2001u,
                /*301*//*pkunwar*/ driver_pkunwar,
                /*302*//*pkunwar*/ driver_pkunwarj,
                /*303*//*gsword*/ driver_gsword,
                /*304*//*shaolins*/ driver_kicker,
                /*305*//*shaolins*/ driver_shaolins,
                /*306*//*stfight*/ driver_empcity,
                /*307*//*stfight*/ driver_empcityj,
                /*308*//*stfight*/ driver_stfight,
                /*309*/
                /*310*//*travrusa*/ driver_travrusa,
                /*311*//*travrusa*/ driver_motorace,
                /*312*//*troangel*/ driver_troangel,
                /* M62 */
                /*313*//*m62*/ driver_kungfum,
                /*314*//*m62*/ driver_kungfud,
                /*315*//*m62*/ driver_spartanx,
                /*316*//*m62*/ driver_kungfub,
                /*317*//*m62*/ driver_kungfub2,
                /*318*//*m62*/ driver_ldrun,
                /*319*//*m62*/ driver_ldruna,
                /*320*//*m62*/ driver_ldrun2,
                /*321*//*m62*/ driver_ldrun3,
                /*322*//*m62*/ driver_ldrun4,
                /*323*//*m62*/ driver_lotlot,
                /*324*//*m62*/ driver_kidniki,
                /*325*//*m62*/ driver_yanchamr,
                /*326*//*m62*/ driver_spelunkr,
                /*327*//*m62*/ driver_spelunk2,
                /*328*//*skydiver*/ driver_skydiver,
                /*329*//*mpatrol*/ driver_mpatrol,
                /*330*//*mpatrol*/ driver_mpatrolw,
                /*331*//*mpatrol*/ driver_mranger,
                /*332*//*yard*/ driver_yard,
                /*333*//*yard*/ driver_vsyard,
                /*334*//*yard*/ driver_vsyard2,
                /*335*//*snk*/ driver_athena,
                /*336*//*snk*/ driver_ftsoccer,
                /*337*//*srumbler*/ driver_srumbler,
                /*338*//*srumbler*/ driver_srumblr2,
                /*339*//*srumbler*/ driver_rushcrsh,
                /*340*//*galivan*/ driver_galivan,
                /*341*//*galivan*/ driver_galivan2,
                /*342*//*galivan*/ driver_dangar,
                /*343*//*galivan*/ driver_dangar2,
                /*344*//*galivan*/ driver_dangarb,
                /*345*//*galivan*/ driver_ninjemak,
                /*346*//*galivan*/ driver_youma,
                /*347*//*circusc*/ driver_circusc,
                /*348*//*circusc*/ driver_circusc2,
                /*349*//*circusc*/ driver_circuscc,
                /*350*//*circusc*/ driver_circusce,
                /*351*//*dec8*/ driver_shackled,
                /*352*//*dec8*/ driver_breywood,
                /*353*//*dec8*/ driver_csilver,
                /* "Crazy Climber hardware" games */
                /*354*//*cclimber*/ driver_cclimber,
                /*355*//*cclimber*/ driver_cclimbrj,
                /*356*//*cclimber*/ driver_ccboot,
                /*357*//*cclimber*/ driver_ccboot2,
                /*358*//*cclimber*/ driver_ckong,
                /*359*//*cclimber*/ driver_ckonga,
                /*360*//*cclimber*/ driver_ckongjeu,
                /*361*//*cclimber*/ driver_ckongo,
                /*362*//*cclimber*/ driver_ckongalc,
                /*363*//*cclimber*/ driver_monkeyd,
                /*364*//*cclimber*/ driver_rpatrolb,
                /*365*//*cclimber*/ driver_silvland,
                /*366*//*cclimber*/ driver_swimmer,
                /*367*//*cclimber*/ driver_swimmera,
                /*368*//*cclimber*/ driver_guzzler,
                /*369*//*snk*/ driver_gwar,
                /*370*//*snk*/ driver_gwarj,
                /*371*//*snk*/ driver_gwarb,
                /*372*//*snk*/ driver_psychos,
                /*373*//*snk*/ driver_psychosj,
                /*374*//*snk*/ driver_chopper,
                /*375*//*snk*/ driver_legofair,
                /*376*//*tehkanwc*/ driver_tehkanwc,
                /*377*//*solomon*/ driver_solomon,
                /*378*//*sauro*/ driver_sauro,
                /* Atari "Missile Command hardware" games */
                /*379*//*missile*/ driver_missile,
                /*380*//*missile*/ driver_missile2,
                /*381*//*missile*/ driver_suprmatk,
                /*382*//*scobra*/ driver_moonwar2,
                /*383*//*scobra*/ driver_monwar2a,
                /*384*//*arkanoid*/ driver_arkatayt,
                /*385*//*arkanoid*/ driver_arkbloc2,
                /*386*//*arkanoid*/ driver_arkangc,
                /*387*//*zaxxon*/ driver_razmataz,
                /*388*//*goindol*/ driver_goindol,
                /*389*//*goindol*/ driver_homo,
                /*390*//*marineb*/ driver_marineb,
                /*391*//*marineb*/ driver_changes,
                /*392*//*marineb*/ driver_looper,
                /*393*//*marineb*/ driver_springer,
                /*394*//*marineb*/ driver_hoccer,
                /*395*//*marineb*/ driver_hoccer2,
                /*396*//*marineb*/ driver_hopprobo,
                /*397*//*marineb*/ driver_wanted,
                /*398*//*ccastles*/ driver_ccastles,
                /*399*//*ccastles*/ driver_ccastle2,
                /*400*//*centiped*/ driver_centiped,
                /*401*//*centiped*/ driver_centipd2,
                /*402*//*centiped*/ driver_centipdb,
                /*403*//*centiped*/ driver_centipb2,
                /*404*//*higemaru*/ driver_higemaru,
                /*405*//*fastfred*/ driver_flyboyb,
                /*406*//*fastfred*/ driver_fastfred,
                /*407*//*fastfred*/ driver_jumpcoas,
                /*408*//*zodiack*/ driver_zodiack,
                /*409*//*zodiack*/ driver_dogfight,
                /*410*//*zodiack*/ driver_moguchan,
                /*411*//*zodiack*/ driver_percuss,
                /*412*//*kingobox*/ driver_kingofb,
                /*413*//*kingobox*/ driver_ringking,
                /* Taito SJ System games */
                /*414*//*taitosj*/ driver_spaceskr,
                /*415*//*taitosj*/ driver_junglek,
                /*416*//*taitosj*/ driver_junglkj2,
                /*417*//*taitosj*/ driver_jungleh,
                /*418*//*taitosj*/ driver_alpine,
                /*419*//*taitosj*/ driver_alpinea,
                /*420*//*taitosj*/ driver_timetunl,
                /*421*//*taitosj*/ driver_wwestern,
                /*422*//*taitosj*/ driver_wwester1,
                /*423*//*taitosj*/ driver_elevatob,
                /*424*//*taitosj*/ driver_bioatack,
                /*425*//*taitosj*/ driver_hwrace,
                /*426*//*blueprnt*/ driver_blueprnt,
                /*427*//*blueprnt*/ driver_blueprnj,
                /*428*//*kchamp*/ driver_kchamp,
                /*429*//*kchamp*/ driver_karatedo,
                /*430*//*kchamp*/ driver_kchampvs,
                /*431*//*kchamp*/ driver_karatevs,
                /*432*//*tutankhm*/ driver_tutankhm,
                /*433*//*tutankhm*/ driver_tutankst,
                /* Mitchell games */
                /*434*//*mitchell*/ driver_mgakuen,
                /*435*//*mitchell*/ driver_mgakuen2,
                /*436*//*mitchell*/ driver_pkladies,
                /*437*//*mitchell*/ driver_pang,
                /*438*//*mitchell*/ driver_pangb,
                /*439*//*mitchell*/ driver_bbros,
                /*440*//*mitchell*/ driver_pompingw,
                /*441*//*mitchell*/ driver_cworld,
                /*442*//*mitchell*/ driver_hatena,
                /*443*//*mitchell*/ driver_spang,
                /*444*//*mitchell*/ driver_sbbros,
                /*445*//*rollerg*/ driver_rollerg,
                /*446*//*rollerg*/ driver_rollergj,
                /*447*//*raiden*/ driver_raiden,
                /*448*//*raiden*/ driver_raidenk,
                /*449*//*snowbros*/ driver_snowbros,
                /*450*//*snowbros*/ driver_snowbroa,
                /*451*//*snowbros*/ driver_snowbrob,
                /*452*//*snowbros*/ driver_snowbroj,
                /*453*//*galpanic*/ driver_galpanic,
                /*454*//*zaxxon*/ driver_zaxxon,
                /*455*//*zaxxon*/ driver_zaxxon2,
                /*456*//*zaxxon*/ driver_zaxxonb,
                /*457*//*zaxxon*/ driver_szaxxon,
                /*458*//*zaxxon*/ driver_futspy,
                /*459*//*congo*/ driver_congo,
                /*460*//*congo*/ driver_tiptop,
                /*461*//*galaga*/ driver_galaga,
                /*462*//*galaga*/ driver_galagamw,
                /*463*//*galaga*/ driver_galagads,
                /*464*//*galaga*/ driver_gallag,
                /*465*//*galaga*/ driver_galagab2,
                /*466*//*galaga*/ driver_galaga84,
                /*467*//*galaga*/ driver_nebulbee,
                /*468*//*rallyx*/ driver_rallyx,
                /*469*//*rallyx*/ driver_rallyx,
                /*470*//*rallyx*/ driver_nrallyx,
                /*471*//*sbasketb*/ driver_sbasketb,
                /*472*//*trackfld*/ driver_trackfld,
                /*473*//*trackfld*/ driver_trackflc,
                /*474*//*trackfld*/ driver_hyprolym,
                /*475*//*hyperspt*/ driver_hyperspt,
                /*476*//*hyperspt*/ driver_hpolym84,
                /*477*//*hyperspt*/ driver_roadf,
                /*478*//*rockrage*/ driver_rockrage,
                /*479*//*rockrage*/ driver_rockragj,
                /*480*//*yiear*/ driver_yiear,
                /*481*//*yiear*/ driver_yiear2,
                /*482*//*astrof*/ driver_astrof,
                /*483*//*astrof*/ driver_astrof2,
                /*484*//*astrof*/ driver_astrof3,
                /*485*//*astrof*/ driver_tomahawk,
                /*486*//*astrof*/ driver_tomahaw5,
                /*487*//*toki*/ driver_tokib,
                /* SNK / Rock-ola games */
                /*488*//*rockola*/ driver_sasuke, /* [1980] Shin Nihon Kikaku (SNK) */
                /*489*//*rockola*/ driver_satansat, /* (c) 1981 SNK */
                /*490*//*rockola*/ driver_zarzon, /* (c) 1981 Taito, gameplay says SNK */
                /*491*//*rockola*/ driver_vanguard, /* (c) 1981 SNK */
                /*492*//*rockola*/ driver_vangrdce, /* (c) 1981 SNK + Centuri */
                /*493*//*rockola*/ driver_fantasy, /* (c) 1981 Rock-ola */
                /*494*//*rockola*/ driver_fantasyj, /* (c) 1981 SNK */
                /*495*//*rockola*/ driver_pballoon, /* (c) 1982 SNK */
                /*496*//*rockola*/ driver_nibbler, /* (c) 1982 Rock-ola */
                /*497*//*rockola*/ driver_nibblera, /* (c) 1982 Rock-ola */
                /*498*//*qix*/ driver_qix,
                /*499*//*qix*/ driver_qixa,
                /*500*//*qix*/ driver_qixb,
                /*501*//*qix*/ driver_qix2,
                /*502*//*ddragon*/ driver_ddragonb,
                /*503*//*ddragon*/ driver_ddragon2,
                /*504*//*vigilant*/ driver_vigilant,
                /*505*//*vigilant*/ driver_vigilntu,
                /*506*//*vigilant*/ driver_vigilntj,
                /*507*//*vigilant*/ driver_kikcubic,
                /*508*//*jackal*/ driver_jackal,
                /*509*//*jackal*/ driver_topgunr,
                /*510*//*jackal*/ driver_jackalj,
                /*511*//*aliens*/ driver_aliens,
                /*512*//*aliens*/ driver_aliens2,
                /*513*//*aliens*/ driver_aliensu,
                /*514*//*aliens*/ driver_aliensj,
                /*515*//*simpsons*/ driver_simpsons,
                /*516*//*simpsons*/ driver_simpsn2p,
                /*517*//*simpsons*/ driver_simps2pj,
                /*518*//*surpratk*/ driver_surpratk,
                /*519*//*vendetta*/ driver_vendetta,
                /*520*//*vendetta*/ driver_vendett2,
                /*521*//*vendetta*/ driver_vendettj,
                /*522*//*crimfght*/ driver_crimfght,
                /*523*//*crimfght*/ driver_crimfgt2,
                /*524*//*crimfght*/ driver_crimfgtj,
                /*snk*/ driver_ikari,
                /*snk*/ driver_ikarijp,
                /*snk*/ driver_ikarijpb,
                /*snk*/ driver_victroad,
                /*snk*/ driver_dogosoke,
                /*marvins*/ driver_marvins,
                /*marvins*/ driver_madcrash,
                /*marvins*/ driver_vangrd2,
                /*trackfld*/ driver_hyprolyb,
                /*snk*/ driver_bermudat,
                /*snk*/ driver_bermudaj,
                /*snk*/ driver_bermudaa,
                /*snk*/ driver_worldwar,
                /*
           Small issues that doesn't affect playable status
                 */
                /*001*//*gladiatr*/ driver_gladiatr, //no nvram support
                /*002*//*gladiatr*/ driver_ogonsiro, //no nvram support
                /*003*//*jailbrek*/ driver_jailbrek, //vlm5030 exists but doesn't work...
                /*004*//*atetris*/ driver_atetris,//no nvram support
                /*005*//*atetris*/ driver_atetrisa,//no nvram support
                /*006*//*atetris*/ driver_atetrisb,//no nvram support
                /*007*//*atetris*/ driver_atetcktl,//no nvram support ,screen needs to be a bit righter
                /*008*//*atetris*/ driver_atetckt2,//no nvram support ,screen needs to be a bit righter

                /*m72*/ driver_bchopper,
                /*m72*/ driver_mrheli,
                /*m72*/ driver_nspirit,
                /*m72*/ driver_nspiritj,
                /*m72*/ driver_imgfight,
                /*m72*/ driver_rtype2,
                /*m72*/ driver_rtype2j,
                /*m72*/ driver_gallop,
                /*
          Possible cpu bugs
                 */
                /*001*//*pacman*/ driver_lizwiz,//reset after a while with CPU #0 PC fe01: warning - op-code execute on mapped i/o ,    CPU #0 PC ff37: warning - op-code execute on mapped i/o 
                /*002*//*skykid*/ driver_skykid,//Freezes before going ingame cpu bug?
                /*m72*/ driver_rtype,//rom ng error?
                /*m72*/ driver_rtypeu, //rom error?
                /*m72*/ driver_rtypeb, //rom error?
                /*m72*/ driver_loht, // not working (unknown)
                /*m72*/ driver_xmultipl, // not working (unknown)
                /*m72*/ driver_dbreed, // not working (unknown)	

                /* GFX issues
        
                 */
                /*001*//*_1943*/ driver_1943kai,/*background islands doesn't draw properly*/
                /*002*//*sidearms*/ driver_dyger, //screen height is too big  
                /*003*//*m62*/ driver_battroad,//screen height is too big
                /*bladestl*/ driver_bladestl, //artifacts left and right of drawing screen
                /*bladestl*/ driver_bladstle, //artifacts left and right of drawing screen
                /*lkage*/ driver_lkage,//garbage remains in left and right of screen (clipping?)
                /*lkage*/ driver_lkageb,//garbage remains in left and right of screen (clipping?)
                /*taitosj*/ driver_waterski,//bad graphics
                /*mitchell*/ driver_block,// wrong window size (height is too large)
                /*mitchell*/ driver_blockj,// wrong window size (height is too large)
                /*mitchell*/ driver_blockbl,// wrong window size (height is too large)

                /*
           Sound issues
                 */
                /*001*//*tecmo*/ driver_rygar, //- crashes ADPCM in generate_adpcm
                /*002*//*tecmo*/ driver_rygar2, //- crashes ADPCM in generate_adpcm
                /*003*//*tecmo*/ driver_rygarj, //- crashes ADPCM in generate_adpcm
                /*004*//*pacland*/ driver_pacland, //no sound at all , namco sound doesn't work
                /*005*//*pacland*/ driver_pacland2,//no sound at all , namco sound doesn't work
                /*006*//*pacland*/ driver_pacland3,//no sound at all , namco sound doesn't work
                /*007*//*pacland*/ driver_paclandm,//no sound at all , namco sound doesn't work
                /*008*//*skykid*/ driver_drgnbstr,//no sound at all , namco sound doesn't work
                /*sidepckt*/ driver_sidepckt,//sound plays too fast	
                /*sidepckt*/ driver_sidepctj,//sound plays too fast	
                /*sidepckt*/ driver_sidepctb,//sound plays too fast
                /*dec8*/ driver_ghostb,//sound plays too fast , small gfx issues
                /*dec8*/ driver_ghostb3,//sound plays too fast , small gfx issues
                /*dec8*/ driver_meikyuh,//sound plays too fast
                /*dec8*/ driver_srdarwin,//sound plays too fast
                /*dec8*/ driver_gondo,//sound plays too fast
                /*dec8*/ driver_makyosen,//sound plays too fast
                /*dec8*/ driver_garyoret,//sound plays too fast , small gfx issues
                /*dec8*/ driver_cobracom,//sound plays too fast
                /*dec8*/ driver_cobracmj,//sound plays too fast

                /*ddrible*/ driver_ddribble, //vlm5030 works but have glitches
                /*blueprnt*/ driver_saturn,//sound partialy works
                /*raiden*/ driver_raidena, //no sound (not in mame 0.36 as well)

                /* controls issues
        
                 */
                /*dec8*/ driver_lastmiss, //no controls working
                /*dec8*/ driver_lastmss2,//no controls working
                /*btime*/ driver_lnc,//no controls working
                /*btime*/ driver_zoar,//no controls working
                /*btime*/ driver_btime,//no controls working
                /*btime*/ driver_btime2,//no controls working
                /*btime*/ driver_btimem,//no controls working
                /*btime*/ driver_wtennis,//no controls working
                /*btime*/ driver_brubber,//no controls working
                /*btime*/ driver_bnj,//no controls working
                /*btime*/ driver_caractn,//no controls working
                /*btime*/ driver_disco,//no controls working
                /*btime*/ driver_mmonkey,//no controls working
                /*btime*/ driver_cookrace,//no controls working,
                /*mystston*/ driver_mystston, //no controls working
                /*docastle*/ driver_docastle,//no controls working
                /*docastle*/ driver_docastl2,//no controls working
                /*docastle*/ driver_douni,//no controls working
                /*docastle*/ driver_dorunrun,//no controls working
                /*docastle*/ driver_dorunru2,//no controls working
                /*docastle*/ driver_dorunruc,//no controls working
                /*docastle*/ driver_spiero,//no controls working
                /*docastle*/ driver_dowild,//no controls working
                /*docastle*/ driver_jjack,//no controls working
                /*docastle*/ driver_kickridr,//no controls working

                /*
           Unknown issues 
                 */
                /*001*//*system1*/ driver_brain, // Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: 2079 at vidhrdw.system1.system1_draw_fg(system1.java:468)
                /*002*//*system1*/ driver_tokisens, // Game Freezes at startup
                /*003*//*scramble*/ driver_mars,//-CPU #0 PC 7702: warning - op-code execute on mapped i/o
                /*004*//*scramble*/ driver_devilfsh,//doesn't start..
                /*005*//*scramble*/ driver_newsin7, //-CPU #1 PC e8a1: warning - op-code execute on mapped i/o
                /*006*//*airbustr*/ driver_airbustr,//boots but doesn't start
                /*dec8*/ driver_oscar,//games doesn't boot and freezes emu
                /*dec8*/ driver_oscarj,//games doesn't boot and freezes emu      		
                /*tehkanwc*/ driver_gridiron,//boots but doesn't start
                /*baraduke*/ driver_baraduke,//stops on booting
                /*baraduke*/ driver_metrocrs,//goes to intro menu but not much after
                /*mnight*/ driver_mnight,//boots but doesn't start,freezes emu
                /*mnight*/ driver_arkarea,///boots but doesn't start,freezes emu
                /*kingobox*/ driver_ringkin3,//warning - op-code execute on mapped i/o
                /*jackal*/ driver_topgunbl, //Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: 51 at vidhrdw.jackal$1.handler(jackal.java:45)
                /*mitchell*/ driver_dokaben,//displays an ram error message
                /*mitchell*/ driver_marukin,//displays an no nvram error message
                /*mitchell*/ driver_qtono1,//not sure if it works  (japanese)
                /*mitchell*/ driver_qsangoku,//not sure if it works (japanese)
                /*m72*/ driver_majtitle, //Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: 786432 at arcadeflex.ptrlib$UBytePtr.read(ptrlib.java:96) at mame.drawgfx.readbit(drawgfx.java:33)
                /*m72*/ driver_hharry, //Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: 4096 at mame.memory.cpu_readmem20(memory.java:1089)
                /*m72*/ driver_hharryu, //Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: 4136 at mame.memory.cpu_readmem20(memory.java:1089)
                /*m72*/ driver_dkgensan, //Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: 4136 at mame.memory.cpu_readmem20(memory.java:1089)

                /*
          GAME NOT WORKING FLAG
                 */
                /*001*//*superqix*/ driver_superqix,
                /*002*//*pacman*/ driver_jumpshot,
                /*003*//*kangaroo*/ driver_fnkyfish,
                /*004*//*system1*/ driver_shtngmst,
                /*005*//*system1*/ driver_chplft,
                /*006*//*system1*/ driver_wboy3,
                /*007*//*system1*/ driver_gardia,
                /*008*//*system1*/ driver_gardiab,
                /*009*//*system1*/ driver_blockgal,
                /*010*//*system1*/ driver_blckgalb,
                /*011*//*system1*/ driver_wbmlj,
                /*012*//*system1*/ driver_wbmlj2,
                /*013*//*system1*/ driver_dakkochn,
                /*014*//*system1*/ driver_ufosensi,
                /*015*//*scobra*/ driver_darkplnt,
                /*016*//*spy*/ driver_spy, //sound works , there are a few gfx problems and missing collision(mame 0.36 doesn't have collision either)   
                /*017//*ironhors*/ driver_farwest,
                /*018*//*lwings*/ driver_avengers,
                /*019*//*lwings*/ driver_avenger2,
                /*020*//*bublbobl*/ driver_tokio,
                /*021*//*snk*/ driver_gwara,
                /*tehkanwc*/ driver_teedoff,
                /*aeroboto*/ driver_formatz,
                /*aeroboto*/ driver_aeroboto,
                /*fastfred*/ driver_flyboy,
                /*kingobox*/ driver_ringkin2,
                /*taitosj*/ driver_kikstart,
                /*ddragon*/ driver_ddragon,
                /*toki*/ driver_toki,
                /*toki*/ driver_toki2,
                /*toki*/ driver_toki3,
                /*toki*/ driver_tokiu,
                /*
        WIP drivers
                 */
                /*xain*/ driver_xsleena, //freezes after a while ym2203 issue? (works okay if i remove sound)
                /*xain*/ driver_xsleenab, //freezes after a while ym2203 issue? (works okay if i remove sound)
                /*xain*/ driver_solarwar, //freezes after a while ym2203 issue? (works okay if i remove sound)
                /*taitosj*/ driver_frontlin,//missing m68705
                /*taitosj*/ driver_elevator,//missing m68705
                /*taitosj*/ driver_tinstar,//missing m68705
                /*taitosj*/ driver_sfposeid,//missing m68705

                /**
                 * *************************************************************
                 * TO RECHECK
                 */
                /**
                 * Small tilemap issues
                 */
                /*gberet*/ driver_gberet, //garbage remains in left and right of screen (clipping?)
                /*gberet*/ driver_rushatck, //garbage remains in left and right of screen (clipping?)
                /*gberet*/ driver_gberetb, //garbage remains in left and right of screen (clipping?)	
                /*gberet*/ driver_mrgoemon, //garbage remains in left and right of screen (clipping?)
                /*mainevt*/ driver_mainevt,//small garbage left and right of screen
                /*mainevt*/ driver_mainevt2,//small garbage left and right of screen
                /*mainevt*/ driver_ringohja,//small garbage left and right of screen
                /*mainevt*/ driver_devstors,//small garbage in right of screen
                /*mainevt*/ driver_devstor2,//small garbage in right of screen
                /*mainevt*/ driver_devstor3,//small garbage in right of screen
                /*mainevt*/ driver_garuka,//small garbage in right of screen

                /**
                 * Tilemaps issues
                 */
                /**
                 * Games not working (various reasons)
                 */
                /*exprraid*/ driver_exprraid, // Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: 4110 at mame.memory.cpu_writemem16(memory.java:1279)
                /*exprraid*/ driver_wexpress, // Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: 4110 at mame.memory.cpu_writemem16(memory.java:1279)
                /*exprraid*/ driver_wexpresb, // Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: 4110 at mame.memory.cpu_writemem16(memory.java:1279)
                /*hyperspt*/ driver_roadf2, //Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: 65536 at arcadeflex.libc$UBytePtr.read(libc.java:86)

                /**
                 * Test (Not working) drivers
                 */
                /*btime*/ //TESTDRIVER( decocass )

                /**
                 * WIP section
                 */
                /*arkanoid*/ driver_arkanoid,
                /*arkanoid*/ driver_arknoidu,
                /*arkanoid*/ driver_arknoidj,
                /*bublbobl*/ driver_bublbobl,//M68705  
                /*bublbobl*/ driver_bublbobr,//M68705  
                /*bublbobl*/ driver_bubbobr1,//M68705  

                /*psychic5*/ driver_psychic5,
                /*bottom9*/ driver_bottom9,
                /*bottom9*/ driver_bottom9n,
                /*88games*/ driver_88games,
                /*88games*/ driver_konami88,
                /*88games*/ driver_hypsptsp,
                /*blockhl*/ driver_blockhl,
                /*blockhl*/ driver_quarth,
                /*thunderx*/ driver_scontra,
                /*thunderx*/ driver_scontraj,
                /*gbusters*/ driver_gbusters,
                /*gbusters*/ driver_crazycop,
                /*tnzs*/ driver_extrmatn,
                /*tnzs*/ driver_arkanoi2,
                /*tnzs*/ driver_ark2us,
                /*tnzs*/ driver_ark2jp,
                /*tnzs*/ driver_plumppop,
                /*tnzs*/ driver_drtoppel,
                /*tnzs*/ driver_chukatai,
                /*tnzs*/ driver_tnzs,
                /*tnzs*/ driver_tnzsb,
                /*tnzs*/ driver_tnzs2,
                /*tnzs*/ driver_insectx,
                /*tnzs*/ driver_kageki,
                /*tnzs*/ driver_kagekij,
                /*matmania*/ driver_matmania,
                /*matmania*/ driver_excthour,
                /*matmania*/ driver_maniach,
                /*matmania*/ driver_maniach2,
                /*renegade*/ driver_renegade,//no sprites?
                /*renegade*/ driver_kuniokun,//no sprites?
                /*renegade*/ driver_kuniokub,//no sprites?

                /*flkatck*/ driver_mx5000,
                /*flkatck*/ driver_flkatck,
                /*slapfght*/ driver_tigerh,
                /*slapfght*/ driver_tigerh2,
                /*slapfght*/ driver_tigerhj,
                /*slapfght*/ driver_slapfigh,
                /*slapfght*/ driver_alcon,
                /*slapfght*/ driver_getstar,
                /*slapfght*/ driver_getstarj,
                /*slapfght*/ driver_tigerhb1,
                /*slapfght*/ driver_tigerhb2,
                /*slapfght*/ driver_slapbtjp,
                /*slapfght*/ driver_slapbtuk,
                /*slapfght*/ driver_getstarb,
                driver_contra, /* GX633 (c) 1987 */
                driver_contrab, /* bootleg */
                driver_contraj, /* GX633 (c) 1987 (Japan) */
                driver_contrajb, /* bootleg */
                driver_gryzor, /* GX633 (c) 1987 */
                driver_combasc, /* GX611 (c) 1988 */
                driver_combasct, /* GX611 (c) 1987 */
                driver_combascj, /* GX611 (c) 1987 (Japan) */
                driver_bootcamp, /* GX611 (c) 1987 */
                driver_combascb, /* bootleg */
                driver_cheekyms,
                /* "Midway" Z80 b/w games */
                driver_astinvad, /* (c) 1980 Stern */
                driver_kamikaze, /* Leijac Corporation */
                driver_spaceint, /* [1980] Shoei */
                driver_wardner, /* TP-009 (c) 1987 Taito Corporation Japan (World) */
                driver_pyros, /* TP-009 (c) 1987 Taito America Corporation (US) */
                driver_wardnerj, /* TP-009 (c) 1987 Taito Corporation (Japan) */
                driver_shootout, /* (c) 1985 Data East USA (US) */
                driver_shootouj, /* (c) 1985 Data East USA (Japan) */
                driver_shootoub, /* bootleg */
                driver_foodf,
                driver_bjtwin,
                /*sf1.java*/ driver_sf1,
                /*sf1.java*/ driver_sf1us,
                /*sf1.java*/ driver_sf1jp,
                /*rastan.java*/ driver_rastan,
                /*rastan.java*/ driver_rastanu,
                /*rastan.java*/ driver_rastanu2,
                /*rastan.java*/ driver_rastsaga,
                /*rainbow.java*/ driver_rainbow,
                /*rainbow.java*/ driver_rainbowe,
                /*rainbow.java*/ driver_jumping,
                driver_cabal,
                driver_cabal2,
                driver_cabalbl,
                driver_xmen,
                driver_xmen6p,
                driver_xmen2pj,
                /*TODO*///      driver_cbasebal,	/* 10/1989 (c) 1989 Capcom (Japan) (different hardware) */
                /* "Galaxian hardware" games */
                driver_galaxian, /* (c) Namco */
                driver_galmidw, /* (c) Midway */
                driver_superg, /* hack */
                driver_galaxb, /* bootleg */
                driver_galapx, /* hack */
                driver_galap1, /* hack */
                driver_galap4, /* hack */
                driver_galturbo, /* hack */
                driver_swarm, /* hack */
                driver_pisces, /* ? */
                driver_uniwars, /* (c) Irem */
                driver_gteikoku, /* (c) Irem */
                driver_spacbatt, /* bootleg */
                driver_warofbug, /* (c) 1981 Armenia */
                driver_redufo, /* ? */
                driver_pacmanbl, /* bootleg */
                driver_devilfsg, /* (c) 1984 Vision / Artic (bootleg?) */
                driver_zigzag, /* (c) 1982 LAX */
                driver_zigzag2, /* (c) 1982 LAX */
                driver_jumpbug, /* (c) 1981 Rock-ola */
                driver_jumpbugb, /* (c) 1981 Sega */
                driver_levers, /* (c) 1983 Rock-ola */
                driver_azurian, /* (c) 1982 Rait Electronics Ltd */
                driver_orbitron, /* Signatron USA */
                driver_mooncrgx, /* bootleg */
                driver_mooncrst, /* (c) 1980 Nichibutsu */
                driver_mooncrsg, /* (c) 1980 Gremlin */
                driver_smooncrs, /* Gremlin */
                driver_mooncrsb, /* bootleg */
                driver_mooncrs2, /* bootleg */
                driver_fantazia, /* bootleg */
                driver_eagle, /* (c) Centuri */
                driver_eagle2, /* (c) Centuri */
                driver_moonqsr, /* (c) 1980 Nichibutsu */
                driver_checkman, /* (c) 1982 Zilec-Zenitone */
                driver_checkmaj, /* (c) 1982 Jaleco (Zenitone/Zilec in ROM CM4, and the programmer names) */
                driver_streakng, /* [1980] Shoei */
                driver_blkhole, /* TDS (Tokyo Denshi Sekkei) */
                driver_moonal2, /* Nichibutsu */
                driver_moonal2b, /* Nichibutsu */
                driver_kingball, /* (c) 1980 Namco */
                //driver_sakingbalj,	/* (c) 1980 Namco */

                /* "Scramble hardware" (and variations) games */
                /*TODO*///driver_scramblb,
                /*TODO*/ //	DRIVER( hunchbks )	/* (c) 1983 Century */

                /* Nichibutsu games */
                /*TODO*/ //	DRIVER( friskyt )	/* (c) 1981 */
                /*TODO*/ //	DRIVER( radrad )	/* (c) 1982 Nichibutsu USA */
                /*TODO*/ //	DRIVER( seicross )	/* (c) 1984 + Alice */
                /*TODO*/ //	DRIVER( sectrzon )	/* (c) 1984 + Alice */
                /*TODO*/ //	DRIVER( wiping )	/* (c) 1982 */
                /*TODO*/ //	DRIVER( rugrats )	/* (c) 1983 */
                /*TODO*/ //	DRIVER( cop01 )		/* (c) 1985 */
                /*TODO*/ //	DRIVER( cop01a )	/* (c) 1985 */
                driver_terracre, /* (c) 1985 */
                driver_terracrb, /* (c) 1985 */
                driver_terracra, /* (c) 1985 */
                /*TODO*/ //	DRIVER( terraf )	/* (c) 1987 */
                /*TODO*/ //	DRIVER( terrafu )	/* (c) 1987 Nichibutsu USA */
                /*TODO*/ //	DRIVER( kodure )	/* (c) 1987 (Japan) */
                /*TODO*/ //	DRIVER( armedf )	/* (c) 1988 */
                /*TODO*/ //	DRIVER( cclimbr2 )	/* (c) 1988 (Japan) */

                /* "Phoenix hardware" (and variations) games */
                /*TODO*/ //	DRIVER( phoenix )	/* (c) 1980 Amstar */
                /*TODO*/ //	DRIVER( phoenixa )	/* (c) 1980 Amstar + Centuri license */
                /*TODO*/ //	DRIVER( phoenixt )	/* (c) 1980 Taito */
                /*TODO*/ //	DRIVER( phoenix3 )	/* bootleg */
                /*TODO*/ //	DRIVER( phoenixc )	/* bootleg */
                /*TODO*/ //	DRIVER( pleiads )	/* (c) 1981 Tehkan */
                /*TODO*/ //	DRIVER( pleiadbl )	/* bootleg */
                /*TODO*/ //	DRIVER( pleiadce )	/* (c) 1981 Centuri + Tehkan */
                /*TODO*/ //	DRIVER( naughtyb )	/* (c) 1982 Jaleco */
                /*TODO*/ //	DRIVER( naughtya )	/* bootleg */
                /*TODO*/ //	DRIVER( naughtyc )	/* (c) 1982 Jaleco + Cinematronics */
                /*TODO*/ //	DRIVER( popflame )	/* (c) 1982 Jaleco */
                /*TODO*/ //	DRIVER( popflama )	/* (c) 1982 Jaleco */
                /*TODO*/ //TESTDRIVER( popflamb )

                /* Namco games (plus some intruders on similar hardware) */
                /*TODO*/ //	DRIVER( geebee )	/* [1978] Namco */
                /*TODO*/ //	DRIVER( geebeeg )	/* [1978] Gremlin */
                /*TODO*/ //	DRIVER( bombbee )	/* [1979] Namco */
                /*TODO*/ //	DRIVER( cutieq )	/* (c) 1979 Namco */
                /*TODO*/ //	DRIVER( navalone )	/* (c) 1980 Namco */
                /*TODO*/ //	DRIVER( kaitei )	/* [1980] K.K. Tokki */
                /*TODO*/ //	DRIVER( kaitein )	/* [1980] Namco */
                /*TODO*/ //	DRIVER( sos )		/* [1980] Namco */
                /*TODO*/ //	DRIVER( tankbatt )	/* (c) 1980 Namco */
                /*TODO*/ //	DRIVER( warpwarp )	/* (c) 1981 Namco */
                /*TODO*/ //	DRIVER( warpwarr )	/* (c) 1981 Rock-ola - the high score table says "NAMCO" */
                /*TODO*/ //	DRIVER( warpwar2 )	/* (c) 1981 Rock-ola - the high score table says "NAMCO" */

                /* the following ones all have a custom I/O chip */
                /*TODO*/ //	DRIVER( bosco )		/* (c) 1981 */
                /*TODO*/ //	DRIVER( boscoo )	/* (c) 1981 */
                /*TODO*/ //	DRIVER( boscomd )	/* (c) 1981 Midway */
                /*TODO*/ //	DRIVER( boscomdo )	/* (c) 1981 Midway */

                /*TODO*/ //	DRIVER( xevious )	/* (c) 1982 */
                /*TODO*/ //	DRIVER( xeviousa )	/* (c) 1982 + Atari license */
                /*TODO*/ //	DRIVER( xevios )	/* bootleg */
                /*TODO*/ //	DRIVER( sxevious )	/* (c) 1984 */

                /*TODO*/ //	DRIVER( grobda )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( grobda2 )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( grobda3 )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( gaplus )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( gaplusa )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( galaga3 )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( galaga3a )	/* (c) 1984 */
                /* Z8000 games */
                /*TODO*/ //	DRIVER( polepos )	/* (c) 1982  */
                /*TODO*/ //	DRIVER( poleposa )	/* (c) 1982 + Atari license */
                /*TODO*/ //	DRIVER( polepos1 )	/* (c) 1982 Atari */
                /*TODO*/ //	DRIVER( topracer )	/* bootleg */
                /*TODO*/ //	DRIVER( polepos2 )	/* (c) 1983 */
                /*TODO*/ //	DRIVER( poleps2a )	/* (c) 1983 + Atari license */
                /*TODO*/ //	DRIVER( poleps2b )	/* bootleg */
                /*TODO*/ //	DRIVER( poleps2c )	/* bootleg */
                /* no custom I/O in the following, HD63701 (or compatible) microcontroller instead */
                /* Namco System 86 games */
                /*TODO*/ //	DRIVER( hopmappy )	/* (c) 1986 */
                /*TODO*/ //	DRIVER( skykiddx )	/* (c) 1986 */
                /*TODO*/ //	DRIVER( skykiddo )	/* (c) 1986 */
                /*TODO*/ //	DRIVER( roishtar )	/* (c) 1986 */
                /*TODO*/ //	DRIVER( genpeitd )	/* (c) 1986 */
                /*TODO*/ //	DRIVER( rthunder )	/* (c) 1986 new version */
                /*TODO*/ //	DRIVER( rthundro )	/* (c) 1986 old version */
                /*TODO*/ //	DRIVER( wndrmomo )	/* (c) 1987 */

                /* Namco System 1 games */
                /*TODO*/ //	DRIVER( shadowld )	/* (c) 1987 */
                /*TODO*/ //	DRIVER( youkaidk )	/* (c) 1987 (Japan new version) */
                /*TODO*/ //	DRIVER( yokaidko )	/* (c) 1987 (Japan old version) */
                /*TODO*/ //	DRIVER( dspirit )	/* (c) 1987 new version */
                /*TODO*/ //	DRIVER( dspirito )	/* (c) 1987 old version */
                /*TODO*/ //	DRIVER( blazer )	/* (c) 1987 (Japan) */
                /*TODO*/ //	DRIVER( quester )	/* (c) 1987 (Japan) */
                /*TODO*/ //	DRIVER( pacmania )	/* (c) 1987 */
                /*TODO*/ //	DRIVER( pacmanij )	/* (c) 1987 (Japan) */
                /*TODO*/ //	DRIVER( galaga88 )	/* (c) 1987 */
                /*TODO*/ //	DRIVER( galag88b )	/* (c) 1987 */
                /*TODO*/ //	DRIVER( galag88j )	/* (c) 1987 (Japan) */
                /*TODO*/ //	DRIVER( ws )		/* (c) 1988 (Japan) */
                /*TODO*/ //	DRIVER( berabohm )	/* (c) 1988 (Japan) */
                /* 1988 Alice in Wonderland (English version of Marchen maze) */
                /*TODO*/ //	DRIVER( mmaze )		/* (c) 1988 (Japan) */
                /*TODO*/ //TESTDRIVER( bakutotu )	/* (c) 1988 */
                /*TODO*/ //	DRIVER( wldcourt )	/* (c) 1988 (Japan) */
                /*TODO*/ //	DRIVER( splatter )	/* (c) 1988 (Japan) */
                /*TODO*/ //	DRIVER( faceoff )	/* (c) 1988 (Japan) */
                /*TODO*/ //	DRIVER( rompers )	/* (c) 1989 (Japan) */
                /*TODO*/ //	DRIVER( romperso )	/* (c) 1989 (Japan) */
                /*TODO*/ //	DRIVER( blastoff )	/* (c) 1989 (Japan) */
                /*TODO*/ //	DRIVER( ws89 )		/* (c) 1989 (Japan) */
                /*TODO*/ //	DRIVER( dangseed )	/* (c) 1989 (Japan) */
                /*TODO*/ //	DRIVER( ws90 )		/* (c) 1990 (Japan) */
                /*TODO*/ //	DRIVER( pistoldm )	/* (c) 1990 (Japan) */
                /*TODO*/ //	DRIVER( soukobdx )	/* (c) 1990 (Japan) */
                /*TODO*/ //	DRIVER( puzlclub )	/* (c) 1990 (Japan) */
                /*TODO*/ //	DRIVER( tankfrce )	/* (c) 1991 (US) */
                /*TODO*/ //	DRIVER( tankfrcj )	/* (c) 1991 (Japan) */

                /* Namco System 2 games */
                /*TODO*/ //TESTDRIVER( finallap )	/* 87.12 Final Lap */
                /*TODO*/ //TESTDRIVER( finalapd )	/* 87.12 Final Lap */
                /*TODO*/ //TESTDRIVER( finalapc )	/* 87.12 Final Lap */
                /*TODO*/ //TESTDRIVER( finlapjc )	/* 87.12 Final Lap */
                /*TODO*/ //TESTDRIVER( finlapjb )	/* 87.12 Final Lap */
                /*TODO*/ //	DRIVER( assault )	/* (c) 1988 */
                /*TODO*/ //	DRIVER( assaultj )	/* (c) 1988 (Japan) */
                /*TODO*/ //	DRIVER( assaultp )	/* (c) 1988 (Japan) */
                /*TODO*/ //TESTDRIVER( metlhawk )	/* (c) 1988 */
                /*TODO*/ //	DRIVER( mirninja )	/* (c) 1988 (Japan) */
                /*TODO*/ //	DRIVER( ordyne )	/* (c) 1988 */
                /*TODO*/ //	DRIVER( phelios )	/* (c) 1988 (Japan) */
                /*TODO*/ //	DRIVER( burnforc )	/* (c) 1989 (Japan) */
                /*TODO*/ //TESTDRIVER( dirtfoxj )	/* (c) 1989 (Japan) */
                /*TODO*/ //	DRIVER( finehour )	/* (c) 1989 (Japan) */
                /*TODO*/ //TESTDRIVER( fourtrax )	/* 89.11 */
                /*TODO*/ //	DRIVER( marvland )	/* (c) 1989 (US) */
                /*TODO*/ //	DRIVER( marvlanj )	/* (c) 1989 (Japan) */
                /*TODO*/ //	DRIVER( valkyrie )	/* (c) 1989 (Japan) */
                /*TODO*/ //	DRIVER ( kyukaidk )	/* (c) 1990 (Japan) */
                /*TODO*/ //	DRIVER ( kyukaido )	/* (c) 1990 (Japan) */
                /*TODO*/ //	DRIVER( dsaber )	/* (c) 1990 */
                /*TODO*/ //	DRIVER( dsaberj )	/* (c) 1990 (Japan) */
                /*TODO*/ //	DRIVER( rthun2 )	/* (c) 1990 */
                /*TODO*/ //	DRIVER( rthun2j )	/* (c) 1990 (Japan) */
                /*TODO*/ //TESTDRIVER( finalap2 )	/* 90.8  Final Lap 2 */
                /*TODO*/ //TESTDRIVER( finalp2j )	/* 90.8  Final Lap 2 (Japan) */
                /* 91.3  Steel Gunner */
                /* 91.7  Golly Ghost */
                /* 91.9  Super World Stadium */
                /*TODO*/ //TESTDRIVER( sgunner2 )	/* (c) 1991 (Japan) */
                /*TODO*/ //	DRIVER( cosmogng )	/* (c) 1991 (US) */
                /*TODO*/ //	DRIVER( cosmognj )	/* (c) 1991 (Japan) */
                /*TODO*/ //TESTDRIVER( finalap3 )	/* 92.9  Final Lap 3 */
                /*TODO*/ //TESTDRIVER( suzuka8h )
                /* 92.8  Bubble Trouble */
                /*TODO*/ //	DRIVER( sws92 )		/* (c) 1992 (Japan) */
                /* 93.4  Lucky & Wild */
                /*TODO*/ //TESTDRIVER( suzuk8h2 )
                /*TODO*/ //	DRIVER( sws93 )		/* (c) 1993 (Japan) */
                /* 93.6  Super World Stadium '93 */
                /* Universal games */
                /*TODO*/ //	DRIVER( cosmicg )	/* 7907 (c) 1979 */
                /*TODO*/ //	DRIVER( cosmica )	/* 7910 (c) [1979] */
                /*TODO*/ //	DRIVER( cosmica2 )	/* 7910 (c) 1979 */
                /*TODO*/ //	DRIVER( panic )		/* (c) 1980 */
                /*TODO*/ //	DRIVER( panica )	/* (c) 1980 */
                /*TODO*/ //	DRIVER( panicger )	/* (c) 1980 */
                /*TODO*/ //	DRIVER( magspot2 )	/* 8013 (c) [1980] */
                /*TODO*/ //	DRIVER( devzone )	/* 8022 (c) [1980] */
                /*TODO*/ //	DRIVER( nomnlnd )	/* (c) [1980?] */
                /*TODO*/ //	DRIVER( nomnlndg )/	* (c) [1980?] + Gottlieb */

                /* Nintendo games */
                /*TODO*/ //	DRIVER( radarscp )	/* (c) 1980 Nintendo */
                /*TODO*/ //	DRIVER( dkong )		/* (c) 1981 Nintendo of America */
                /*TODO*/ //	DRIVER( dkongjp )	/* (c) 1981 Nintendo */
                /*TODO*/ //	DRIVER( dkongjpo )	/* (c) 1981 Nintendo */
                /*TODO*/ //	DRIVER( dkongjr )	/* (c) 1982 Nintendo of America */
                /*TODO*/ //	DRIVER( dkngjrjp )	/* no copyright notice */
                /*TODO*/ //	DRIVER( dkjrjp )	/* (c) 1982 Nintendo */
                /*TODO*/ //	DRIVER( dkjrbl )	/* (c) 1982 Nintendo of America */
                /*TODO*/ //	DRIVER( dkong3 )	/* (c) 1983 Nintendo of America */
                /*TODO*/ //	DRIVER( dkong3j )	/* (c) 1983 Nintendo */
                /*TODO*/ //                driver_mario,	/* (c) 1983 Nintendo of America */
                /*TODO*/ //                driver_mariojp,	/* (c) 1983 Nintendo */
                /*TODO*/ //                driver_masao,	/* bootleg */
                /*TODO*/ //	DRIVER( hunchbkd )	/* (c) 1983 Century */
                /*TODO*/ //	DRIVER( herbiedk )	/* (c) 1984 CVS */
                /*TODO*/ //TESTDRIVER( herocast )
                /*TODO*/ //	DRIVER( popeye )
                /*TODO*/ //	DRIVER( popeye2 )
                /*TODO*/ //	DRIVER( popeyebl )	/* bootleg */
                driver_punchout, /* (c) 1984 */
                driver_spnchout, /* (c) 1984 */
                driver_spnchotj, /* (c) 1984 (Japan) */
                driver_armwrest, /* (c) 1985 */
                /* Midway 8080 b/w games */
                /*TODO*/ //	DRIVER( seawolf )	/* 596 [1976] */
                /*TODO*/ //	DRIVER( gunfight )	/* 597 [1975] */
                /* 603 - Top Gun [1976] */
                /*TODO*/ //	DRIVER( tornbase )	/* 605 [1976] */
                /*TODO*/ //	DRIVER( 280zzzap )	/* 610 [1976] */
                /*TODO*/ //	DRIVER( maze )		/* 611 [1976] */
                /*TODO*/ //	DRIVER( boothill )	/* 612 [1977] */
                /*TODO*/ //	DRIVER( checkmat )	/* 615 [1977] */
                /*TODO*/ //	DRIVER( desertgu )	/* 618 [1977] */
                /*TODO*/ //	DRIVER( dplay )		/* 619 [1977] */
                /*TODO*/ //	DRIVER( lagunar )	/* 622 [1977] */
                /*TODO*/ //	DRIVER( gmissile )	/* 623 [1977] */
                /*TODO*/ //	DRIVER( m4 )		/* 626 [1977] */
                /*TODO*/ //	DRIVER( clowns )	/* 630 [1978] */
                /* 640 - Space Walk [1978] */
                /*TODO*/ //	DRIVER( einnings )	/* 642 [1978] Midway */
                /*TODO*/ //	DRIVER( shuffle )	/* 643 [1978] */
                /*TODO*/ //	DRIVER( dogpatch )	/* 644 [1977] */
                /*TODO*/ //	DRIVER( spcenctr )	/* 645 (c) 1980 Midway */
                /*TODO*/ //	DRIVER( phantom2 )	/* 652 [1979] */
                /*TODO*/ //	DRIVER( bowler )	/* 730 [1978] Midway */
                /*TODO*/ //	DRIVER( invaders )	/* 739 [1979] */
                /*TODO*/ //	DRIVER( blueshrk )	/* 742 [1978] */
                /*TODO*/ //	DRIVER( invad2ct )	/* 851 (c) 1980 Midway */
                /*TODO*/ //	DRIVER( invadpt2 )	/* 852 [1980] Taito */
                /*TODO*/ //	DRIVER( invdpt2m )	/* 852 [1980] Midway */
                /* 870 - Space Invaders Deluxe cocktail */
                /*TODO*/ //	DRIVER( earthinv )
                /*TODO*/ //	DRIVER( spaceatt )
                /*TODO*/ //	DRIVER( sinvemag )
                /*TODO*/ //	DRIVER( jspecter )
                /*TODO*/ //	DRIVER( invrvnge )
                /*TODO*/ //	DRIVER( invrvnga )
                /*TODO*/ //	DRIVER( galxwars )
                /*TODO*/ //	DRIVER( starw )
                /*TODO*/ //	DRIVER( lrescue )	/* (c) 1979 Taito */
                /*TODO*/ //	DRIVER( grescue )	/* bootleg? */
                /*TODO*/ //	DRIVER( desterth )	/* bootleg */
                /*TODO*/ //	DRIVER( cosmicmo )	/* Universal */
                /*TODO*/ //	DRIVER( rollingc )	/* Nichibutsu */
                /*TODO*/ //	DRIVER( bandido )	/* (c) Exidy */
                /*TODO*/ //	DRIVER( ozmawars )	/* Shin Nihon Kikaku (SNK) */
                /*TODO*/ //	DRIVER( solfight )	/* bootleg */
                /*TODO*/ //	DRIVER( spaceph )	/* Zilec Games */
                /*TODO*/ //	DRIVER( schaser )	/* Taito */
                /*TODO*/ //	DRIVER( lupin3 )	/* (c) 1980 Taito */
                /*TODO*/ //	DRIVER( helifire )	/* (c) Nintendo */
                /*TODO*/ //	DRIVER( helifira )	/* (c) Nintendo */
                /*TODO*/ //	DRIVER( spacefev )
                /*TODO*/ //	DRIVER( sfeverbw )
                /*TODO*/ //	DRIVER( spclaser )
                /*TODO*/ //	DRIVER( laser )
                /*TODO*/ //	DRIVER( spcewarl )
                /*TODO*/ //	DRIVER( polaris )	/* (c) 1980 Taito */
                /*TODO*/ //	DRIVER( polarisa )	/* (c) 1980 Taito */
                /*TODO*/ //	DRIVER( ballbomb )	/* (c) 1980 Taito */
                /*TODO*/ //	DRIVER( m79amb )
                /*TODO*/ //	DRIVER( alieninv )
                /*TODO*/ //	DRIVER( sitv )
                /*TODO*/ //	DRIVER( sicv )
                /*TODO*/ //	DRIVER( sisv )
                /*TODO*/ //	DRIVER( sisv2 )
                /*TODO*/ //	DRIVER( spacewr3 )
                /*TODO*/ //	DRIVER( invaderl )
                /*TODO*/ //	DRIVER( yosakdon )
                /*TODO*/ //	DRIVER( spceking )
                /*TODO*/ //	DRIVER( spcewars )

                /* Meadows S2650 games */
                /*TODO*/ //	DRIVER( lazercmd )	/* [1976?] */
                /*TODO*/ //	DRIVER( deadeye )	/* [1978?] */
                /*TODO*/ //	DRIVER( gypsyjug )	/* [1978?] */
                /*TODO*/ //	DRIVER( medlanes )	/* [1977?] */

                /* Midway "Astrocade" games */
                /*TODO*/ //	DRIVER( wow )		/* (c) 1980 */
                /*TODO*/ //	DRIVER( robby )		/* (c) 1981 */
                /*TODO*/ //	DRIVER( gorf )		/* (c) 1981 */
                /*TODO*/ //	DRIVER( gorfpgm1 )	/* (c) 1981 */
                /*TODO*/ //	DRIVER( seawolf2 )
                /*TODO*/ //	DRIVER( spacezap )	/* (c) 1980 */
                /*TODO*/ //	DRIVER( ebases )

                /* Bally Midway MCR games */
                /* MCR1 */
                /*TODO*/ //	DRIVER( solarfox )	/* (c) 1981 */
                /*TODO*/ //	DRIVER( kick )		/* (c) 1981 */
                /*TODO*/ //	DRIVER( kicka )		/* bootleg? */
                /* MCR2 */
                /*TODO*/ //	DRIVER( shollow )	/* (c) 1981 */
                /*TODO*/ //	DRIVER( shollow2 )	/* (c) 1981 */
                /*TODO*/ //	DRIVER( tron )		/* (c) 1982 */
                /*TODO*/ //	DRIVER( tron2 )		/* (c) 1982 */
                /*TODO*/ //	DRIVER( kroozr )	/* (c) 1982 */
                /*TODO*/ //	DRIVER( domino )	/* (c) 1982 */
                /*TODO*/ //	DRIVER( wacko )		/* (c) 1982 */
                /*TODO*/ //	DRIVER( twotiger )	/* (c) 1984 */
                /* MCR2 + MCR3 sprites */
                /*TODO*/ //	DRIVER( journey )	/* (c) 1983 */
                /* MCR3 */
                /*TODO*/ //	DRIVER( tapper )	/* (c) 1983 */
                /*TODO*/ //	DRIVER( tappera )	/* (c) 1983 */
                /*TODO*/ //	DRIVER( sutapper )	/* (c) 1983 */
                /*TODO*/ //	DRIVER( rbtapper )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( timber )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( dotron )	/* (c) 1983 */
                /*TODO*/ //	DRIVER( dotrone )	/* (c) 1983 */
                /*TODO*/ //	DRIVER( destderb )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( destderm )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( sarge )		/* (c) 1985 */
                /*TODO*/ //	DRIVER( rampage )	/* (c) 1986 */
                /*TODO*/ //	DRIVER( rampage2 )	/* (c) 1986 */
                /*TODO*/ //	DRIVER( powerdrv )	/* (c) 1986 */
                /*TODO*/ //	DRIVER( maxrpm )	/* (c) 1986 */
                /*TODO*/ //	DRIVER( spyhunt )	/* (c) 1983 */
                /*TODO*/ //	DRIVER( turbotag )	/* (c) 1985 */
                /*TODO*/ //	DRIVER( crater )	/* (c) 1984 */
                /* MCR 68000 */
                /*TODO*/ //	DRIVER( zwackery )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( xenophob )	/* (c) 1987 */
                /*TODO*/ //	DRIVER( spyhunt2 )	/* (c) 1987 */
                /*TODO*/ //	DRIVER( blasted )	/* (c) 1988 */
                /*TODO*/ //	DRIVER( archrivl )	/* (c) 1989 */
                /*TODO*/ //	DRIVER( archriv2 )	/* (c) 1989 */
                /*TODO*/ //	DRIVER( trisport )	/* (c) 1989 */
                /*TODO*/ //	DRIVER( pigskin )	/* (c) 1990 */

                /* Bally / Sente games */
                /*TODO*/ //	DRIVER( sentetst )
                /*TODO*/ //	DRIVER( cshift )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( gghost )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( hattrick )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( otwalls )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( snakepit )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( snakjack )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( stocker )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( triviag1 )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( triviag2 )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( triviasp )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( triviayp )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( triviabb )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( gimeabrk )	/* (c) 1985 */
                /*TODO*/ //	DRIVER( minigolf )	/* (c) 1985 */
                /*TODO*/ //	DRIVER( minigol2 )	/* (c) 1985 */
                /*TODO*/ //	DRIVER( toggle )	/* (c) 1985 */
                /*TODO*/ //	DRIVER( nametune )	/* (c) 1986 */
                /*TODO*/ //	DRIVER( nstocker )	/* (c) 1986 */
                /*TODO*/ //	DRIVER( sfootbal )	/* (c) 1986 */
                /*TODO*/ //	DRIVER( spiker )	/* (c) 1986 */
                /*TODO*/ //	DRIVER( rescraid )	/* (c) 1987 */

                /* Irem games */
                /* trivia: IREM means "International Rental Electronics Machines" */
                /*TODO*/ //	DRIVER( skychut )	/* (c) [1980] */

                /*m72*/ driver_kengo, //no rom available	
                /*m72*/ driver_poundfor, //no rom available

                /* not M72, but same sound hardware */
                /*TODO*/ //	DRIVER( sichuan2 )	/* (c) 1989 Tamtex */
                /*TODO*/ //	DRIVER( sichuana )	/* (c) 1989 Tamtex */
                /*TODO*/ //	DRIVER( shisen )	/* (c) 1989 Tamtex */
                /* M92 */
                driver_bmaster, /* (c) 1991 Irem */
                driver_gunforce, /* (c) 1991 Irem (World) */
                driver_gunforcu, /* (c) 1991 Irem America (US) */
                driver_hook, /* (c) 1992 Irem (World) */
                driver_hooku, /* (c) 1992 Irem America (US) */
                driver_mysticri, /* (c) 1992 Irem (World) */
                driver_gunhohki, /* (c) 1992 Irem (Japan) */
                driver_uccops, /* (c) 1992 Irem (World) */
                driver_uccopsj, /* (c) 1992 Irem (Japan) */
                driver_rtypeleo, /* (c) 1992 Irem (Japan) */
                driver_majtitl2, /* (c) 1992 Irem (World) */
                driver_skingame, /* (c) 1992 Irem America (US) */
                driver_skingam2, /* (c) 1992 Irem America (US) */
                driver_inthunt, /* (c) 1993 Irem (World) */
                driver_kaiteids, /* (c) 1993 Irem (Japan) */
                /*TODO*/ //TESTDRIVER( nbbatman )	/* (c) 1993 Irem America (US) */
                /*TODO*/ //TESTDRIVER( leaguemn )	/* (c) 1993 Irem (Japan) */
                driver_lethalth, /* (c) 1991 Irem (World) */
                driver_thndblst, /* (c) 1991 Irem (Japan) */
                driver_psoldier, /* (c) 1993 Irem (Japan) */
                /* M97 */
                /*TODO*/ //TESTDRIVER( riskchal )
                /*TODO*/ //TESTDRIVER( gussun )
                /*TODO*/ //TESTDRIVER( shisen2 )
                /*TODO*/ //TESTDRIVER( quizf1 )
                /*TODO*/ //TESTDRIVER( atompunk )
                /*TODO*/ //TESTDRIVER( bbmanw )
                /* M107 */
                /*TODO*/ //TESTDRIVER( firebarr )	/* (c) 1993 Irem (Japan) */
                /*TODO*/ //	DRIVER( dsoccr94 )	/* (c) 1994 Irem (Data East Corporation license) */

                /* Gottlieb/Mylstar games (Gottlieb became Mylstar in 1983) */
                /*TODO*/ //	DRIVER( reactor )	/* GV-100 (c) 1982 Gottlieb */
                /*TODO*/ //	DRIVER( mplanets )	/* GV-102 (c) 1983 Gottlieb */
                /*TODO*/ //	DRIVER( qbert )		/* GV-103 (c) 1982 Gottlieb */
                /*TODO*/ //	DRIVER( qbertjp )	/* GV-103 (c) 1982 Gottlieb + Konami license */
                /*TODO*/ //	DRIVER( sqbert )	/* (c) 1983 Mylstar - never released */
                /*TODO*/ //	DRIVER( krull )		/* GV-105 (c) 1983 Gottlieb */
                /*TODO*/ //	DRIVER( mach3 )		/* GV-109 (c) 1983 Mylstar */
                /*TODO*/ //	DRIVER( usvsthem )	/* GV-??? (c) 198? Mylstar */
                /*TODO*/ //	DRIVER( 3stooges )	/* GV-113 (c) 1984 Mylstar */
                /*TODO*/ //	DRIVER( qbertqub )	/* GV-119 (c) 1983 Mylstar */
                /*TODO*/ //	DRIVER( curvebal )	/* GV-134 (c) 1984 Mylstar */

                /* older Taito games */
                /*TODO*/ //	DRIVER( crbaloon )	/* (c) 1980 Taito Corporation */
                /*TODO*/ //	DRIVER( crbalon2 )	/* (c) 1980 Taito Corporation */

                /* Taito "Qix hardware" games */
                driver_sdungeon,//M68705
                driver_elecyoyo,//M68705
                driver_elecyoy2,//M68705
                driver_kram,//M68705
                driver_kram2,//M68705
                driver_zookeep,//M68705
                driver_zookeep2,//M68705
                driver_zookeep3,//M68705

                /* other Taito games */
                /*TODO*/ //	DRIVER( bking2 )	/* (c) 1983 Taito Corporation */

                /*TODO*/ //	DRIVER( tsamurai )	/* (c) 1985 Taito */
                /*TODO*/ //	DRIVER( tsamura2 )	/* (c) 1985 Taito */
                /*TODO*/ //	DRIVER( nunchaku )	/* (c) 1985 Taito */
                /*TODO*/ //	DRIVER( yamagchi )	/* (c) 1985 Taito */
                /*TODO*/ //TESTDRIVER( flstory )	/* (c) 1985 Taito Corporation */
                /*TODO*/ //TESTDRIVER( flstoryj )	/* (c) 1985 Taito Corporation (Japan) */

                /*TODO*/ //	DRIVER( kicknrun )	/* (c) 1986 Taito Corporation */
                /*TODO*/ //	DRIVER( mexico86 )	/* bootleg (Micro Research) */
                /*TODO*/ //	DRIVER( kikikai )	/* (c) 1986 Taito Corporation */

                /*TODO*/ //TESTDRIVER( arkbl2 )
                /*TODO*/ //TESTDRIVER( arkbl3 )	/* bootleg */

                /*TODO*/ //TESTDRIVER( arkblock )	/* bootleg */

                driver_superman,
                /*TODO*/ //TESTDRIVER( footchmp )	/* (c) 1990 Taito Corporation Japan (World) */

                /* Taito L-System games */
                /*TODO*/ //	DRIVER( fhawk )		/* (c) 1988 Taito Corporation (Japan) */
                /*TODO*/ //	DRIVER( raimais )	/* (c) 1988 Taito Corporation (Japan) */
                /*TODO*/ //	DRIVER( champwr )	/* (c) 1989 Taito Corporation Japan (World) */
                /*TODO*/ //	DRIVER( champwru )	/* (c) 1989 Taito America Corporation (US) */
                /*TODO*/ //	DRIVER( champwrj )	/* (c) 1989 Taito Corporation (Japan) */
                /*TODO*/ //	DRIVER( puzznic )	/* (c) 1989 Taito Corporation (Japan) */
                /*TODO*/ //	DRIVER( plotting )	/* (c) 1989 Taito Corporation Japan (World) */
                /*TODO*/ //	DRIVER( palamed )	/* (c) 1990 Taito Corporation (Japan) */
                /*TODO*/ //	DRIVER( horshoes )	/* (c) 1990 Taito America Corporation (US) */
                /*TODO*/ //	DRIVER( cachat )	/* (c) 1993 Taito Corporation (Japan) */

                /* Taito F2 games */
                driver_ssi, /* (c) 1990 Taito Corporation Japan (World) */
                /* Majestic 12 (c) 1990 Taito America Corporation (US) */
                driver_majest12, /* (c) 1990 Taito Corporation (Japan) */
                /*TODO*/ //TESTDRIVER( finalb )
                /*TODO*/ //TESTDRIVER( megab )
                driver_liquidk, /* (c) 1990 Taito Corporation Japan (World) */
                driver_liquidku, /* (c) 1990 Taito America Corporation (US) */
                driver_mizubaku, /* (c) 1990 Taito Corporation (Japan) */
                driver_growl, /* (c) 1990 Taito Corporation Japan (World) */
                driver_growlu, /* (c) 1990 Taito America Corporation (US) */
                driver_runark, /* (c) 1990 Taito Corporation (Japan) */
                /* Toaplan games */
                /*TODO*/ //	DRIVER( fshark )	/* TP-007 (c) 1987 Taito Corporation (World) */
                /*TODO*/ //	DRIVER( skyshark )	/* TP-007 (c) 1987 Taito America Corporation + Romstar license (US) */
                /*TODO*/ //	DRIVER( hishouza )	/* TP-007 (c) 1987 Taito Corporation (Japan) */
                /*TODO*/ //	DRIVER( fsharkbt )	/* bootleg */

                /*TODO*/ //	DRIVER( twincobr )	/* TP-011 (c) 1987 Taito Corporation (World) */
                /*TODO*/ //	DRIVER( twincobu )	/* TP-011 (c) 1987 Taito America Corporation + Romstar license (US) */
                /*TODO*/ //	DRIVER( ktiger )	/* TP-011 (c) 1987 Taito Corporation (Japan) */

                driver_rallybik, /* TP-012 (c) 1988 Taito */
                driver_truxton, /* TP-013B (c) 1988 Taito */
                driver_hellfire, /* TP-??? (c) 1989 Toaplan + Taito license */
                driver_zerowing, /* TP-015 (c) 1989 Toaplan */
                driver_demonwld, /* TP-016 (c) 1989 Toaplan + Taito license */
                driver_fireshrk, /* TP-017 (c) 1990 Toaplan */
                driver_samesame, /* TP-017 (c) 1989 Toaplan */
                driver_outzone, /* TP-018 (c) 1990 Toaplan */
                driver_outzonep, /* bootleg */
                driver_vimana,/* TP-019 (c) 1991 Toaplan (+ Tecmo license when set to Japan) */
                driver_vimana2, /* TP-019 (c) 1991 Toaplan (+ Tecmo license when set to Japan)  */
                driver_vimanan, /* TP-019 (c) 1991 Toaplan (+ Nova Apparate GMBH & Co license) */
                driver_tekipaki, /* TP-020 (c) 1991 Toaplan */
                driver_ghox, /* TP-021 (c) 1991 Toaplan */
                driver_dogyuun, /* TP-022 (c) 1992 Toaplan */
                driver_kbash, /* TP-023 (c) 1993 Toaplan */
                /*TODO*/ //TESTDRIVER( tatsujn2 )	/* TP-024 */
                driver_pipibibs, /* TP-025 */
                /*TODO*/ //TESTDRIVER( pipibibi )	/* bootleg */
                driver_whoopee, /* TP-025 */
                /*TODO*/ //TESTDRIVER( fixeight )	/* TP-026 (c) 1992 + Taito license */
                driver_vfive, /* TP-027 (c) 1993 Toaplan */
                driver_batsugun, /* TP-030 (c) 1993 Toaplan */
                driver_snowbro2, /* TP-??? (c) 1994 Hanafram */
                /* Williams games */
                /*TODO*/ //	DRIVER( defender )	/* (c) 1980 */
                /*TODO*/ //	DRIVER( defendg )	/* (c) 1980 */
                /*TODO*/ //	DRIVER( defendw )	/* (c) 1980 */
                /*TODO*/ //TESTDRIVER( defndjeu )	/* bootleg */
                /*TODO*/ //	DRIVER( defcmnd )	/* bootleg */
                /*TODO*/ //TESTDRIVER( defcomnd )	/* bootleg */
                /*TODO*/ //	DRIVER( defence )	/* bootleg */
                /*TODO*/ //	DRIVER( mayday )
                /*TODO*/ //	DRIVER( maydaya )
                /*TODO*/ //	DRIVER( colony7 )	/* (c) 1981 Taito */
                /*TODO*/ //	DRIVER( colony7a )	/* (c) 1981 Taito */
                /*TODO*/ //	DRIVER( stargate )	/* (c) 1981 */
                /*TODO*/ //	DRIVER( robotron )	/* (c) 1982 */
                /*TODO*/ //	DRIVER( robotryo )	/* (c) 1982 */
                /*TODO*/ //	DRIVER( joust )		/* (c) 1982 */
                /*TODO*/ //	DRIVER( joustr )	/* (c) 1982 */
                /*TODO*/ //	DRIVER( joustwr )	/* (c) 1982 */
                /*TODO*/ //	DRIVER( bubbles )	/* (c) 1982 */
                /*TODO*/ //	DRIVER( bubblesr )	/* (c) 1982 */
                /*TODO*/ //	DRIVER( splat )		/* (c) 1982 */
                /*TODO*/ //	DRIVER( sinistar )	/* (c) 1982 */
                /*TODO*/ //	DRIVER( sinista1 )	/* (c) 1982 */
                /*TODO*/ //	DRIVER( sinista2 )	/* (c) 1982 */
                /*TODO*/ //	DRIVER( blaster )	/* (c) 1983 */
                /*TODO*/ //	DRIVER( mysticm )	/* (c) 1983 */
                /*TODO*/ //	DRIVER( tshoot )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( inferno )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( joust2 )	/* (c) 1986 */

                /* Capcom games */
                /* The following is a COMPLETE list of the Capcom games up to 1997, as shown on */
                /* their web site. The list is sorted by production date. */
                /*TODO*/ //	DRIVER( bionicc )	/*  3/1987 (c) 1987 (US) */
                /*TODO*/ //	DRIVER( bionicc2 )	/*  3/1987 (c) 1987 (US) */
                /*TODO*/ //	DRIVER( topsecrt )	/*  3/1987 (c) 1987 (Japan) */

                driver_tigeroad, /* 11/1987 (c) 1987 + Romstar (US) */
                driver_toramich, /* 11/1987 (c) 1987 (Japan) */
                driver_f1dream, /*  4/1988 (c) 1988 + Romstar */
                driver_f1dreamb, /* bootleg */
                driver_lastduel, /*  7/1988 (c) 1988 (US) */
                driver_lstduela, /*  7/1988 (c) 1988 (US) */
                driver_lstduelb, /* bootleg */
                driver_madgear, /*  2/1989 (c) 1989 (US) */
                driver_madgearj, /*  2/1989 (c) 1989 (Japan) */
                driver_ledstorm, /*  2/1989 (c) 1989 (US) */
                /* Capcom CPS1 games */
                driver_forgottn, //coin counter work with 7 instead of 5
                driver_lostwrld, //coin counter work with 7 instead of 5
                driver_ghouls,//appears to work ok
                driver_ghoulsu,//appears to work ok
                driver_ghoulsj,//Unsupported readrom() swappage
                driver_strider,
                driver_striderj,
                driver_stridrja,
                driver_dwj,//coin counter work with 7 instead of 5 . Life decreasement has bug (goes from 900 to 999)(68k bug)
                driver_willow,//seems to work ok
                driver_willowj,
                driver_unsquad,
                driver_area88,
                driver_ffight,
                driver_ffightu,
                driver_ffightj,
                driver_1941,
                driver_1941j,
                driver_mercs,
                driver_mercsu,
                driver_mercsj,
                driver_mtwins,
                driver_chikij,
                driver_msword,
                driver_mswordu,
                driver_mswordj,
                driver_cawing,
                driver_cawingj,
                driver_nemo,
                driver_nemoj,
                driver_sf2,
                driver_sf2a,
                driver_sf2b,
                driver_sf2e,
                driver_sf2j,
                driver_sf2jb,
                driver_3wonders,
                driver_wonder3,
                driver_kod, //seems ok
                driver_kodj,
                driver_kodb,
                driver_captcomm,//coin counter work with 7 instead of 5 
                driver_captcomu,
                driver_captcomj,
                driver_knights,
                driver_knightsj,
                driver_sf2ce,
                driver_sf2cea,
                driver_sf2ceb,
                driver_sf2cej,
                driver_sf2rb,
                driver_sf2red,
                driver_sf2accp2,
                driver_varth,
                driver_varthu,
                driver_varthj,
                driver_cworld2j,
                driver_wof,
                driver_wofj,
                driver_sf2t,
                driver_sf2tj,
                driver_dino,
                driver_dinoj,
                driver_punisher,
                driver_punishru,
                driver_punishrj,
                driver_slammast,
                driver_mbomberj,
                driver_mbombrd,
                driver_mbombrdj,
                driver_pnickj,
                driver_qad,
                driver_qadj,
                driver_qtono2,
                driver_pang3,
                driver_megaman, //seems ok
                driver_rockmanj,//seems ok
                driver_sfzch, //no roms?

                /* Capcom CPS2 games */
                /* list completed by CPS2Shock */
                /* http://cps2shock.retrogames.com */
                /*TODO*/ //TESTDRIVER( ssf2 )		/* Super Street Fighter 2: The New Challengers (USA 930911) */
                /*TODO*/ //TESTDRIVER( ssf2a )		/* Super Street Fighter 2: The New Challengers (Asia 930911) */
                /*TODO*/ //TESTDRIVER( ssf2j )		/* Super Street Fighter 2: The New Challengers (Japan 930910) */
                /*TODO*/ //TESTDRIVER( ecofe )		/* Eco Fighters (Etc 931203) */
                /*TODO*/ //TESTDRIVER( ddtod )		/* Dungeons & Dragons: Tower of Doom (USA 940113) */
                /*TODO*/ //TESTDRIVER( ddtoda )	/* Dungeons & Dragons: Tower of Doom (Asia 940113) */
                /*TODO*/ //TESTDRIVER( ddtodr1 )	/* Dungeons & Dragons: Tower of Doom (USA 940125) */
                /*TODO*/ //TESTDRIVER( ssf2t )		/* Super Street Fighter 2 Turbo (USA 940223) */
                /*TODO*/ //TESTDRIVER( ssf2xj )	/* Super Street Fighter 2 X: Grand Master Challenge (Japan 940223) */
                /*TODO*/ //TESTDRIVER( avsp )		/* Aliens Vs. Predator (USA 940520) */
                /*TODO*/ //TESTDRIVER( vampj )		/* Vampire: The Night Warriors (Japan 940705) */
                /*TODO*/ //TESTDRIVER( vampa )		/* Vampire: The Night Warriors (Asia 940705) */
                /*TODO*/ //TESTDRIVER( dstlk )		/* DarkStalkers: The Night Warriors (USA 940818) */
                /*TODO*/ //TESTDRIVER( slam2e )	/* Saturday Night Slammasters II: Ring of Destruction (Euro 940902) */
                /*TODO*/ //TESTDRIVER( armwara )	/* Armoured Warriors (Asia 940920) */
                /*TODO*/ //TESTDRIVER( xmcotaj )	/* X-Men: Children of the Atom (Japan 941219) */
                /*TODO*/ //TESTDRIVER( xmcota )	/* X-Men: Children of the Atom (USA 950105) */
                /*TODO*/ //TESTDRIVER( vhuntj )	/* Vampire Hunter: Darkstalkers 2 (Japan 950302) */
                /*TODO*/ //TESTDRIVER( nwarr )		/* Night Warriors: DarkStalkers Revenge (USA 950406) */
                /*TODO*/ //TESTDRIVER( cybotsj )	/* Cyberbots: Full Metal Madness (Japan 950420) */
                /*TODO*/ //TESTDRIVER( sfa )		/* Street Fighter Alpha: The Warriors Dream (USA 950627) */
                /*TODO*/ //TESTDRIVER( sfar1 )		/* Street Fighter Alpha: The Warriors Dream (USA 950727) */
                /*TODO*/ //TESTDRIVER( sfzj )		/* Street Fighter Zero (Japan 950627) */
                /*TODO*/ //TESTDRIVER( sfzjr1 )	/* Street Fighter Zero (Japan 950727) */
                /*TODO*/ //TESTDRIVER( msh )		/* Marvel Super Heroes (USA 951024) */
                /*TODO*/ //TESTDRIVER( 19xx )		/* 19XX: The Battle Against Destiny (USA 951207) */
                /*TODO*/ //TESTDRIVER( ddsom )		/* Dungeons & Dragons 2: Shadow over Mystara (USA 960209) */
                /*TODO*/ //TESTDRIVER( sfz2j )		/* Street Fighter Zero 2 (Japan 960227) */
                /*TODO*/ //TESTDRIVER( spf2xj )	/* Super Puzzle Fighter 2 X (Japan 960531) */
                /*TODO*/ //TESTDRIVER( spf2t )		/* Super Puzzle Fighter 2 Turbo (USA 960620) */
                /*TODO*/ //TESTDRIVER( rckman2j )	/* Rockman 2: The Power Fighters (Japan 960708) */
                /*TODO*/ //TESTDRIVER( sfz2a )		/* Street Fighter Zero 2 Alpha (Japan 960805) */
                /*  9/1996 Quiz Naneiro Dreams */
                /*TODO*/ //TESTDRIVER( xmvsf )		/* X-Men Vs. Street Fighter (USA 961004) */
                /*TODO*/ //TESTDRIVER( batcirj )	/* Battle Circuit (Japan 970319) */
                /*TODO*/ //TESTDRIVER( batcira )	/* Battle Circuit (Asia 970319) */
                /*TODO*/ //TESTDRIVER( vsav )		/* Vampire Savior: The Lord of Vampire (USA 970519) */
                /*TODO*/ //TESTDRIVER( vsavj )		/* Vampire Savior: The Lord of Vampire (Japan 970519) */
                /*TODO*/ //TESTDRIVER( mshvsf )	/* Marvel Super Heroes Vs. Street Fighter (USA 970625) */
                /*TODO*/ //TESTDRIVER( mshvsfj )	/* Marvel Super Heroes Vs. Street Fighter (Japan 970707) */
                /*TODO*/ //TESTDRIVER( vhunt2 )	/* Vampire Hunter 2: Darkstalkers Revenge (Japan 970828) */
                /*TODO*/ //TESTDRIVER( sgemf )		/* Super Gem Fighter Mini Mix (USA 970904) */
                /*TODO*/ //TESTDRIVER( pfghtj )	/* Pocket Fighter (Japan 970904) */
                /*TODO*/ //TESTDRIVER( vsav2 )		/* Vampire Savior 2: The Lord of Vampire (Japan 970913) */
                /*TODO*/ //TESTDRIVER( mvsc )		/* Marvel Super Heroes vs. Capcom: Clash of Super Heroes (USA 980123) */
                /*TODO*/ //TESTDRIVER( sfa3 )		/* Street Fighter Alpha 3 (USA 980629) */
                /* 1999 Giga Wing */
                /* Gulum Pa! */
                /* Capcom ZN1/ZN2 games */
                /*TODO*/ //TESTDRIVER( ts2j )		/*  Battle Arena Toshinden 2 (JAPAN 951124) */
                /*  7/1996 Star Gladiator */
                /*TODO*/ //TESTDRIVER( sfex )		/*  Street Fighter EX (ASIA 961219) */
                /*TODO*/ //TESTDRIVER( sfexj )		/*  Street Fighter EX (JAPAN 961130) */
                /*TODO*/ //TESTDRIVER( sfexp )		/*  Street Fighter EX Plus (USA 970311) */
                /*TODO*/ //TESTDRIVER( sfexpj )	/*  Street Fighter EX Plus (JAPAN 970311) */
                /*TODO*/ //TESTDRIVER( rvschool )	/*  Rival Schools (ASIA 971117) */
                /*TODO*/ //TESTDRIVER( jgakuen )	/*  Justice Gakuen (JAPAN 971117) */
                /*TODO*/ //TESTDRIVER( sfex2 )		/*  Street Fighter EX 2 (JAPAN 980312) */
                /*TODO*/ //TESTDRIVER( tgmj )		/*  Tetris The Grand Master (JAPAN 980710) */
                /*TODO*/ //TESTDRIVER( sfex2p )	/*  Street Fighter EX 2 Plus (JAPAN 990611) */
                /*  Star Gladiator 2 */
                /*  Rival Schools 2 */
                /* Incredible Technologies games */
                /*TODO*/ //	DRIVER( capbowl )	/* (c) 1988 Incredible Technologies */
                /*TODO*/ //	DRIVER( capbowl2 )	/* (c) 1988 Incredible Technologies */
                /*TODO*/ //	DRIVER( clbowl )	/* (c) 1989 Incredible Technologies */
                /*TODO*/ //	DRIVER( bowlrama )	/* (c) 1991 P & P Marketing */

                /* Leland games */
                /*TODO*/ //TESTDRIVER( mayhem )	/* (c) 1985 Cinematronics */
                /*TODO*/ //TESTDRIVER( wseries )	/* (c) 1985 Cinematronics Inc. */
                /*TODO*/ //TESTDRIVER( dangerz )	/* (c) 1986 Cinematronics USA Inc. */
                /*TODO*/ //TESTDRIVER( basebal2 )	/* (c) 1987 Cinematronics Inc. */
                /*TODO*/ //TESTDRIVER( dblplay )	/* (c) 1987 Tradewest / The Leland Corp. */
                /*TODO*/ //TESTDRIVER( teamqb )	/* (c) 1988 Leland Corp. */
                /*TODO*/ //TESTDRIVER( strkzone )	/* (c) 1988 The Leland Corporation */
                /*TODO*/ //TESTDRIVER( offroad )	/* (c) 1989 Leland Corp. */
                /*TODO*/ //TESTDRIVER( offroadt )
                /*TODO*/ //TESTDRIVER( pigout )	/* (c) 1990 The Leland Corporation */
                /*TODO*/ //TESTDRIVER( pigoutj )	/* (c) 1990 The Leland Corporation */
                /*TODO*/ //TESTDRIVER( redlin2p )
                /*TODO*/ //TESTDRIVER( viper )
                /*TODO*/ //TESTDRIVER( aafb )
                /*TODO*/ //TESTDRIVER( aafb2p )
                /*TODO*/ //TESTDRIVER( aafbu )
                /*TODO*/ //TESTDRIVER( alleymas )
                /*TODO*/ //TESTDRIVER( cerberus )
                /*TODO*/ //TESTDRIVER( ataxx )
                /*TODO*/ //TESTDRIVER( ataxxa )
                /*TODO*/ //TESTDRIVER( indyheat )
                /*TODO*/ //TESTDRIVER( wsf )

                /* Gremlin 8080 games */
                /* the numbers listed are the range of ROM part numbers */
                /*TODO*/ //	DRIVER( blockade )	/* 1-4 [1977 Gremlin] */
                /*TODO*/ //	DRIVER( comotion )	/* 5-7 [1977 Gremlin] */
                /*TODO*/ //	DRIVER( hustle )	/* 16-21 [1977 Gremlin] */
                /*TODO*/ //	DRIVER( blasto )	/* [1978 Gremlin] */

                /* Gremlin/Sega "VIC dual game board" games */
                /* the numbers listed are the range of ROM part numbers */
                /*TODO*/ //	DRIVER( depthch )	/* 50-55 [1977 Gremlin?] */
                /*TODO*/ //	DRIVER( safari )	/* 57-66 [1977 Gremlin?] */
                /*TODO*/ //	DRIVER( frogs )		/* 112-119 [1978 Gremlin?] */
                /*TODO*/ //	DRIVER( sspaceat )	/* 155-162 (c) */
                /*TODO*/ //	DRIVER( sspacatc )	/* 139-146 (c) */
                /*TODO*/ //	DRIVER( headon )	/* 163-167/192-193 (c) Gremlin */
                /*TODO*/ //	DRIVER( headonb )	/* 163-167/192-193 (c) Gremlin */
                /*TODO*/ //	DRIVER( headon2 )	/* ???-??? (c) 1979 Sega */
                /* ???-??? Fortress */
                /* ???-??? Gee Bee */
                /* 255-270  Head On 2 / Deep Scan */
                /*TODO*/ //	DRIVER( invho2 )	/* 271-286 (c) 1979 Sega */
                /*TODO*/ //	DRIVER( samurai )	/* 289-302 + upgrades (c) 1980 Sega */
                /*TODO*/ //	DRIVER( invinco )	/* 310-318 (c) 1979 Sega */
                /*TODO*/ //	DRIVER( invds )		/* 367-382 (c) 1979 Sega */
                /*TODO*/ //	DRIVER( tranqgun )	/* 413-428 (c) 1980 Sega */
                /* 450-465  Tranquilizer Gun (different version?) */
                /* ???-??? Car Hunt / Deep Scan */
                /*TODO*/ //	DRIVER( spacetrk )	/* 630-645 (c) 1980 Sega */
                /*TODO*/ //	DRIVER( sptrekct )	/* (c) 1980 Sega */
                /*TODO*/ //	DRIVER( carnival )	/* 651-666 (c) 1980 Sega */
                /*TODO*/ //	DRIVER( carnvckt )	/* 501-516 (c) 1980 Sega */
                /*TODO*/ //	DRIVER( digger )	/* 684-691 no copyright notice */
                /*TODO*/ //	DRIVER( pulsar )	/* 790-805 (c) 1981 Sega */
                /*TODO*/ //	DRIVER( heiankyo )	/* (c) [1979?] Denki Onkyo */

                /* Sega G-80 vector games */
                /*TODO*/ //	DRIVER( spacfury )	/* (c) 1981 */
                /*TODO*/ //	DRIVER( spacfura )	/* no copyright notice */
                /*TODO*/ //	DRIVER( zektor )	/* (c) 1982 */
                /*TODO*/ //	DRIVER( tacscan )	/* (c) */
                /*TODO*/ //	DRIVER( elim2 )		/* (c) 1981 Gremlin */
                /*TODO*/ //	DRIVER( elim2a )	/* (c) 1981 Gremlin */
                /*TODO*/ //	DRIVER( elim4 )		/* (c) 1981 Gremlin */
                /*TODO*/ //	DRIVER( startrek )	/* (c) 1982 */

                /* Sega G-80 raster games */
                /*TODO*/ //	DRIVER( astrob )	/* (c) 1981 */
                /*TODO*/ //	DRIVER( astrob1 )	/* (c) 1981 */
                /*TODO*/ //	DRIVER( 005 )		/* (c) 1981 */
                /*TODO*/ //	DRIVER( monsterb )	/* (c) 1982 */
                /*TODO*/ //	DRIVER( spaceod )	/* (c) 1981 */
                /*TODO*/ //	DRIVER( pignewt )	/* (c) 1983 */
                /*TODO*/ //	DRIVER( pignewta )	/* (c) 1983 */
                /*TODO*/ //	DRIVER( sindbadm )	/* 834-5244 (c) 1983 Sega */

                /* other Sega 8-bit games */
                /*TODO*/ //	DRIVER( turbo )		/* (c) 1981 Sega */
                /*TODO*/ //	DRIVER( turboa )	/* (c) 1981 Sega */
                /*TODO*/ //	DRIVER( turbob )	/* (c) 1981 Sega */
                /*TODO*/ //TESTDRIVER( kopunch )	/* 834-0103 (c) 1981 Sega */
                /*TODO*/ //	DRIVER( suprloco )	/* (c) 1982 Sega */
                /*TODO*/ //	DRIVER( champbas )	/* (c) 1983 Sega */
                /*TODO*/ //	DRIVER( champbb2 )

                /*TODO*/ //	DRIVER( dotrikun )	/* cabinet test board */
                /*TODO*/ //	DRIVER( dotriku2 )	/* cabinet test board */

                /* Sega System 16 games */
                // Not working
                driver_alexkidd, /* (c) 1986 (protected) */
                /*TODO*/ //	DRIVER( aliensya )	/* (c) 1987 (protected) */
                /*TODO*/ //	DRIVER( aliensyb )	/* (c) 1987 (protected) */
                /*TODO*/ //	DRIVER( aliensyj )	/* (c) 1987 (protected. Japan) */
                /*TODO*/ //	DRIVER( astorm )	/* (c) 1990 (protected) */
                /*TODO*/ //	DRIVER( astorm2p )	/* (c) 1990 (protected 2 Players) */
                /*TODO*/ //	DRIVER( auraila )	/* (c) 1990 Sega / Westone (protected) */
                /*TODO*/ //	DRIVER( bayrouta )	/* (c) 1989 (protected) */
                /*TODO*/ //	DRIVER( bayrtbl1 )	/* (c) 1989 (protected) (bootleg) */
                /*TODO*/ //	DRIVER( bayrtbl2 )	/* (c) 1989 (protected) (bootleg) */
                /*TODO*/ //	DRIVER( enduror )	/* (c) 1985 (protected) */
                /*TODO*/ //	DRIVER( eswat )		/* (c) 1989 (protected) */
                /*TODO*/ //	DRIVER( fpoint )	/* (c) 1989 (protected) */
                /*TODO*/ //	DRIVER( goldnaxb )	/* (c) 1989 (protected) */
                /*TODO*/ //	DRIVER( goldnaxc )	/* (c) 1989 (protected) */
                /*TODO*/ //	DRIVER( goldnaxj )	/* (c) 1989 (protected. Japan) */
                /*TODO*/ //	DRIVER( jyuohki )	/* (c) 1988 (protected. Altered Beast Japan) */
                /*TODO*/ //	DRIVER( moonwalk )	/* (c) 1990 (protected) */
                /*TODO*/ //	DRIVER( moonwlka )	/* (c) 1990 (protected) */
                /*TODO*/ //	DRIVER( passsht )	/* (protected) */
                /*TODO*/ //	DRIVER( sdioj )		/* (c) 1987 (protected. Japan) */
                /*TODO*/ //	DRIVER( shangon )	/* (c) 1992 (protected) */
                /*TODO*/ //	DRIVER( shinobia )	/* (c) 1987 (protected) */
                /*TODO*/ //	DRIVER( shinobib )	/* (c) 1987 (protected) */
                /*TODO*/ //	DRIVER( tetris )	/* (c) 1988 (protected) */
                /*TODO*/ //	DRIVER( tetrisa )	/* (c) 1988 (protected) */
                /*TODO*/ //	DRIVER( wb3a )		/* (c) 1988 Sega / Westone (protected) */

                /*TODO*/ //TESTDRIVER( aceattac )	/* (protected) */
                /*TODO*/ //TESTDRIVER( aburner )	/* */
                /*TODO*/ //TESTDRIVER( aburner2 )  /* */
                /*TODO*/ //TESTDRIVER( afighter )	/* (protected) */
                /*TODO*/ //TESTDRIVER( bloxeed )	/* (protected) */
                /*TODO*/ //TESTDRIVER( cltchitr )	/* (protected) */
                /*TODO*/ //TESTDRIVER( cotton )	/* (protected) */
                /*TODO*/ //TESTDRIVER( cottona )	/* (protected) */
                /*TODO*/ //TESTDRIVER( ddcrew )	/* (protected) */
                /*TODO*/ //TESTDRIVER( dunkshot )	/* (protected) */
                /*TODO*/ //TESTDRIVER( exctleag )  /* (protected) */
                /*TODO*/ //TESTDRIVER( lghost )	/* (protected) */
                /*TODO*/ //TESTDRIVER( loffire )	/* (protected) */
                /*TODO*/ //TESTDRIVER( mvp )		/* (protected) */
                /*TODO*/ //TESTDRIVER( ryukyu )	/* (protected) */
                /*TODO*/ //TESTDRIVER( suprleag )  /* (protected) */
                /*TODO*/ //TESTDRIVER( thndrbld )	/* (protected) */
                /*TODO*/ //TESTDRIVER( thndrbdj )  /* (protected?) */
                /*TODO*/ //TESTDRIVER( toutrun )	/* (protected) */
                /*TODO*/ //TESTDRIVER( toutruna )	/* (protected) */

                // Working
                driver_alexkida, /* (c) 1986 */
                /*TODO*/ //	DRIVER( aliensyn )	/* (c) 1987 */
                /*TODO*/ //	DRIVER( altbeas2 )	/* (c) 1988 */
                driver_altbeast, /* (c) 1988 */
                /*TODO*/ //	DRIVER( astormbl )	/* bootleg */
                driver_atomicp, /* (c) 1990 Philko */
                /*TODO*/ //	DRIVER( aurail )	/* (c) 1990 Sega / Westone */
                /*TODO*/ //	DRIVER( bayroute )	/* (c) 1989 */
                /*TODO*/ //	DRIVER( bodyslam )	/* (c) 1986 */
                driver_dduxbl, /* (c) 1989 (Datsu bootleg) */
                /*TODO*/ //	DRIVER( dumpmtmt )	/* (c) 1986 (Japan) */
                /*TODO*/ //	DRIVER( endurob2 )	/* (c) 1985 (Beta bootleg) */
                /*TODO*/ //	DRIVER( endurobl )	/* (c) 1985 (Herb bootleg) */
                /*TODO*/ //	DRIVER( eswatbl )	/* (c) 1989 (but bootleg) */
                /*TODO*/ //	DRIVER( fantzone )	/* (c) 1986 */
                /*TODO*/ //	DRIVER( fantzono )	/* (c) 1986 */
                driver_fpointbl, /* (c) 1989 (Datsu bootleg) */
                /*TODO*/ //	DRIVER( goldnabl )	/* (c) 1989 (bootleg) */
                /*TODO*/ //	DRIVER( goldnaxa )	/* (c) 1989 */
                driver_goldnaxe, /* (c) 1989 */
                /*TODO*/ //	DRIVER( hangon )	/* (c) 1985 */
                driver_hwchamp, /* (c) 1987 */
                /*TODO*/ //	DRIVER( mjleague )	/* (c) 1985 */
                /*TODO*/ //	DRIVER( moonwlkb )	/* bootleg */
                /*TODO*/ //	DRIVER( outrun )	/* (c) 1986 (bootleg)*/
                /*TODO*/ //	DRIVER( outruna )	/* (c) 1986 (bootleg) */
                /*TODO*/ //	DRIVER( outrunb )	/* (c) 1986 (protected beta bootleg) */
                /*TODO*/ //	DRIVER( passht4b )	/* bootleg */
                /*TODO*/ //	DRIVER( passshtb )	/* bootleg */
                /*TODO*/ //	DRIVER( quartet )	/* (c) 1986 */
                /*TODO*/ //	DRIVER( quartet2 )	/* (c) 1986 */
                /*TODO*/ //	DRIVER( quartetj )	/* (c) 1986 */
                /*TODO*/ //	DRIVER( riotcity )	/* (c) 1991 Sega / Westone */
                driver_sdi, /* (c) 1987 */
                /*TODO*/ //	DRIVER( shangonb )	/* (c) 1992 (but bootleg) */
                /*TODO*/ //	DRIVER( sharrier )	/* (c) 1985 */
                /*TODO*/ //	DRIVER( shdancbl )	/* (c) 1989 (but bootleg) */
                /*TODO*/ //	DRIVER( shdancer )	/* (c) 1989 */
                /*TODO*/ //	DRIVER( shdancrj )	/* (c) 1989 */
                driver_shinobi, /* (c) 1987 */
                /*TODO*/ //	DRIVER( shinobl )	/* (c) 1987 (but bootleg) */
                driver_tetrisbl, /* (c) 1988 (but bootleg) */
                /*TODO*/ //	DRIVER( timscanr )	/* (c) 1987 */
                /*TODO*/ //	DRIVER( toryumon )	/* (c) 1995 */
                /*TODO*/ //	DRIVER( tturf )		/* (c) 1989 Sega / Sunsoft */
                /*TODO*/ //	DRIVER( tturfbl )	/* (c) 1989 (Datsu bootleg) */
                /*TODO*/ //	DRIVER( tturfu )	/* (c) 1989 Sega / Sunsoft */
                driver_wb3, /* (c) 1988 Sega / Westone */
                /*TODO*////driver_wb3bl,	/* (c) 1988 Sega / Westone (but bootleg) */
                /*TODO*/ //	DRIVER( wrestwar )	/* (c) 1989 */

                /* other Data East games */
                /*TODO*/ //	DRIVER( firetrap )	/* (c) 1986 */
                /*TODO*/ //	DRIVER( firetpbl )	/* bootleg */

                /*TODO*/ //	DRIVER( actfancr )	/* (c) 1989 Data East Corporation (World) */
                /*TODO*/ //	DRIVER( actfanc1 )	/* (c) 1989 Data East Corporation (World) */
                /*TODO*/ //	DRIVER( actfancj )	/* (c) 1989 Data East Corporation (Japan) */
                /*TODO*/ //	DRIVER( triothep )	/* (c) 1989 Data East Corporation (Japan) */

                /* Data East 16-bit games */
                driver_karnov, /* (c) 1987 Data East USA (US) */
                driver_karnovj, /* (c) 1987 Data East Corporation (Japan) */
                /*TODO*/ //TESTDRIVER( wndrplnt )	/* (c) 1987 Data East Corporation (Japan) */
                driver_chelnov, /* (c) 1988 Data East USA (US) */
                driver_chelnovj, /* (c) 1988 Data East Corporation (Japan) */
                /* the following ones all run on similar hardware */
                driver_hbarrel,
                driver_hbarrelw,
                driver_baddudes,
                driver_drgninja,
                /*TODO*/ //TESTDRIVER( birdtry )	/* (c) 1988 Data East Corporation (Japan) */
                driver_robocop,
                driver_robocopu,
                driver_robocpu0,
                driver_robocopb,
                driver_hippodrm,
                driver_ffantasy,
                driver_slyspy,
                driver_slyspy2,
                driver_secretag,
                /*TODO*/ //TESTDRIVER( secretab )	/* bootleg */
                driver_midres,
                driver_midresu,
                driver_midresj,
                driver_bouldash,
                /* end of similar hardware */
                /*TODO*/ //	DRIVER( stadhero )	/* (c) 1988 Data East Corporation (Japan) */
                /*TODO*/ //	DRIVER( madmotor )	/* (c) [1989] Mitchell */
                /* All these games have a unique code stamped on the mask roms */
                /*TODO*/ //	DRIVER( vaportra )	/* MAA (c) 1989 Data East Corporation (US) */
                /*TODO*/ //	DRIVER( kuhga )		/* MAA (c) 1989 Data East Corporation (Japan) */
                /*TODO*/ //	DRIVER( cbuster )	/* MAB (c) 1990 Data East Corporation (World) */
                /*TODO*/ //	DRIVER( cbusterw )	/* MAB (c) 1990 Data East Corporation (World) */
                /*TODO*/ //	DRIVER( cbusterj )	/* MAB (c) 1990 Data East Corporation (Japan) */
                /*TODO*/ //	DRIVER( twocrude )	/* MAB (c) 1990 Data East USA (US) */
                /*TODO*/ //	DRIVER( darkseal )	/* MAC (c) 1990 Data East Corporation (World) */
                /*TODO*/ //	DRIVER( darksea1 )	/* MAC (c) 1990 Data East Corporation (World) */
                /*TODO*/ //	DRIVER( darkseaj )	/* MAC (c) 1990 Data East Corporation (Japan) */
                /*TODO*/ //	DRIVER( gatedoom )	/* MAC (c) 1990 Data East Corporation (US) */
                /*TODO*/ //	DRIVER( gatedom1 )	/* MAC (c) 1990 Data East Corporation (US) */
                /*TODO*/ //TESTDRIVER( edrandy )	/* MAD (c) 1990 Data East Corporation (World) */
                /*TODO*/ //TESTDRIVER( edrandyj )	/* MAD (c) 1990 Data East Corporation (Japan) */
                /*TODO*/ //	DRIVER( supbtime )	/* MAE (c) 1990 Data East Corporation (Japan) */
                /* Mutant Fighter/Death Brade MAF (c) 1991 */
                driver_cninja, /* MAG (c) 1991 Data East Corporation (World) */
                /*TODO*/ //	DRIVER( cninja0 )	/* MAG (c) 1991 Data East Corporation (World) */
                /*TODO*/ //	DRIVER( cninjau )	/* MAG (c) 1991 Data East Corporation (US) */
                /*TODO*/ //	DRIVER( joemac )	/* MAG (c) 1991 Data East Corporation (Japan) */
                driver_stoneage, /* bootleg */
                /* Robocop 2           MAH (c) 1991 */
                /* Desert Assault/Thunderzone MAJ (c) 1991 */
                /* Rohga Armour Attack/Wolf Fang MAM (c) 1991 */
                /* Captain America     MAN (c) 1991 */
                driver_tumblep,
                driver_tumblepj,
                driver_tumblepb,
                driver_tumblep2,
                /*TODO*/ //TESTDRIVER( funkyjet )	/* MAT (c) 1992 Mitchell */

                /* Tehkan / Tecmo games (Tehkan became Tecmo in 1986) */
                /*TODO*/ //	DRIVER( senjyo )	/* (c) 1983 Tehkan */
                /*TODO*/ //	DRIVER( starforc )	/* (c) 1984 Tehkan */
                /*TODO*/ //	DRIVER( starfore )	/* (c) 1984 Tehkan */
                /*TODO*/ //	DRIVER( megaforc )	/* (c) 1985 Tehkan + Video Ware license */
                /*TODO*/ //	DRIVER( baluba )	/* (c) 1986 Able Corp. */

                driver_gaiden, /* 6215 - (c) 1988 Tecmo */
                driver_shadoww, /* 6215 - (c) 1988 Tecmo */
                driver_tknight, /* (c) 1989 Tecmo */
                driver_wildfang, /* (c) 1989 Tecmo */
                driver_wc90b, /* bootleg */
                /* Konami bitmap games */
                /*TODO*/ //	DRIVER( junofrst )	/* GX310 (c) 1983 Konami */

                /* Konami games */
                /*TODO*/ //	DRIVER( megazone )	/* GX319 (c) 1983 */
                /*TODO*/ //	DRIVER( megaznik )	/* GX319 (c) 1983 + Interlogic / Kosuka */
                /*TODO*/ //	DRIVER( pandoras )	/* GX328 (c) 1984 + Interlogic */
                /*TODO*/ //	DRIVER( gyruss )	/* GX347 (c) 1983 */
                /*TODO*/ //	DRIVER( gyrussce )	/* GX347 (c) 1983 + Centuri license */
                /*TODO*/ //	DRIVER( venus )		/* bootleg */

                /*TODO*/ //	DRIVER( finalizr )	/* GX523 (c) 1985 */
                /*TODO*/ //	DRIVER( finalizb )	/* bootleg */

                /*TODO*/ //	DRIVER( fastlane )	/* GX752 (c) 1987 */
                /*TODO*/ //	DRIVER( labyrunr )	/* GX771 (c) 1987 (Japan) */

                /*ajax*/ driver_ajax,
                /*ajax*/ driver_ajaxj,
                /*TODO*/ //	DRIVER( thunderx )	/* GX873 (c) 1988 */
                /*TODO*/ //	DRIVER( thnderxj )	/* GX873 (c) 1988 (Japan) */

                /*parodius*/ driver_parodius,
                /*TODO*/ //TESTDRIVER( xexex )		/* GX067 (c) 1991 */

                /*TODO*/ //	DRIVER( wecleman )	/* GX602 (c) 1986 */
                /*TODO*/ //	DRIVER( hotchase )	/* GX763 (c) 1988 */
                /*TODO*/ //	DRIVER( ultraman )	/* GX910 (c) 1991 Banpresto/Bandai */

                /* Konami "Nemesis hardware" games */
                driver_nemesis, /* GX456 (c) 1985 */
                driver_nemesuk, /* GX456 (c) 1985 */
                driver_konamigt, /* GX561 (c) 1985 */
                driver_salamand, /* GX587 (c) 1986 */
                driver_lifefrce, /* GX587 (c) 1986 */
                driver_lifefrcj, /* GX587 (c) 1986 */
                /* GX400 BIOS based games */
                driver_rf2, /* GX561 (c) 1985 */
                driver_twinbee, /* GX412 (c) 1985 */
                driver_gradius, /* GX456 (c) 1985 */
                driver_gwarrior, /* GX578 (c) 1985 */
                /* Konami "Twin 16" games */
                /*TODO*/ //	DRIVER( devilw )	/* GX687 (c) 1987 */
                /*TODO*/ //	DRIVER( darkadv )	/* GX687 (c) 1987 */
                /*TODO*/ //	DRIVER( majuu )		/* GX687 (c) 1987 (Japan) */
                /*TODO*/ //	DRIVER( vulcan )	/* GX785 (c) 1988 */
                /*TODO*/ //	DRIVER( gradius2 )	/* GX785 (c) 1988 (Japan) */
                /*TODO*/ //	DRIVER( grdius2a )	/* GX785 (c) 1988 (Japan) */
                /*TODO*/ //	DRIVER( grdius2b )	/* GX785 (c) 1988 (Japan) */
                /*TODO*/ //	DRIVER( cuebrick )	/* GX903 (c) 1989 */
                /*TODO*/ //	DRIVER( fround )	/* GX870 (c) 1988 */
                /*TODO*/ //	DRIVER( hpuncher )	/* GX870 (c) 1988 (Japan) */
                /*TODO*/ //	DRIVER( miaj )		/* GX808 (c) 1989 (Japan) */

                /* Konami Gradius III board */
                /*TODO*/ //	DRIVER( gradius3 )	/* GX945 (c) 1989 (Japan) */
                /*TODO*/ //	DRIVER( grdius3a )	/* GX945 (c) 1989 (Asia) */

                /* (some) Konami 68000 games */
                driver_mia,
                driver_mia2,
                driver_tmnt, /* GX963 (c) 1989 (US) */
                driver_tmht, /* GX963 (c) 1989 (UK) */
                driver_tmntj, /* GX963 (c) 1989 (Japan) */
                driver_tmht2p, /* GX963 (c) 1989 (UK) */
                driver_tmnt2pj, /* GX963 (c) 1990 (Japan) */
                driver_punkshot, /* GX907 (c) 1990 (US) */
                driver_punksht2, /* GX907 (c) 1990 (US) */
                driver_lgtnfght, /* GX939 (c) 1990 (US) */
                driver_trigon, /* GX939 (c) 1990 (Japan) */
                /*TODO*/ //	DRIVER( blswhstl )	/* GX060 (c) 1991 */
                /*TODO*/ //	DRIVER( detatwin )	/* GX060 (c) 1991 (Japan) */
                /*TODO*/ //TESTDRIVER( glfgreat )	/* GX061 (c) 1991 */
                /*TODO*/ //	DRIVER( tmnt2 )		/* GX063 (c) 1991 (US) */
                /*TODO*/ //	DRIVER( tmnt22p )	/* GX063 (c) 1991 (US) */
                /*TODO*/ //	DRIVER( tmnt2a )	/* GX063 (c) 1991 (Asia) */
                driver_ssriders,
                /*TODO*/ //	DRIVER( ssrdrebd )	/* GX064 (c) 1991 (World) */
                /*TODO*/ //	DRIVER( ssrdrebc )	/* GX064 (c) 1991 (World) */
                /*TODO*/ //	DRIVER( ssrdruda )	/* GX064 (c) 1991 (US) */
                /*TODO*/ //	DRIVER( ssrdruac )	/* GX064 (c) 1991 (US) */
                /*TODO*/ //	DRIVER( ssrdrubc )	/* GX064 (c) 1991 (US) */
                /*TODO*/ //	DRIVER( ssrdrabd )	/* GX064 (c) 1991 (Asia) */
                /*TODO*/ //	DRIVER( ssrdrjbd )	/* GX064 (c) 1991 (Japan) */

                driver_thndrx2, /* GX073 (c) 1991 (Japan) */
                /* Exidy games */
                /*TODO*/ //	DRIVER( sidetrac )	/* (c) 1979 */
                /*TODO*/ //	DRIVER( targ )		/* (c) 1980 */
                /*TODO*/ //	DRIVER( spectar )	/* (c) 1980 */
                /*TODO*/ //	DRIVER( spectar1 )	/* (c) 1980 */
                /*TODO*/ //	DRIVER( venture )	/* (c) 1981 */
                /*TODO*/ //	DRIVER( venture2 )	/* (c) 1981 */
                /*TODO*/ //	DRIVER( venture4 )	/* (c) 1981 */
                /*TODO*/ //	DRIVER( mtrap )		/* (c) 1981 */
                /*TODO*/ //	DRIVER( mtrap3 )	/* (c) 1981 */
                /*TODO*/ //	DRIVER( mtrap4 )	/* (c) 1981 */
                /*TODO*/ //	DRIVER( pepper2 )	/* (c) 1982 */
                /*TODO*/ //	DRIVER( hardhat )	/* (c) 1982 */
                /*TODO*/ //	DRIVER( fax )		/* (c) 1983 */
                /*TODO*/ //	DRIVER( circus )	/* no copyright notice [1977?] */
                /*TODO*/ //	DRIVER( robotbwl )	/* no copyright notice */
                /*TODO*/ //	DRIVER( crash )		/* Exidy [1979?] */
                /*TODO*/ //	DRIVER( ripcord )	/* Exidy [1977?] */
                /*TODO*/ //	DRIVER( starfire )	/* Exidy [1979?] */
                /*TODO*/ //	DRIVER( fireone )	/* (c) 1979 Exidy */

                /* Exidy 440 games */
                /*TODO*/ //	DRIVER( crossbow )	/* (c) 1983 */
                /*TODO*/ //	DRIVER( cheyenne )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( combat )	/* (c) 1985 */
                /*TODO*/ //	DRIVER( cracksht )	/* (c) 1985 */
                /*TODO*/ //	DRIVER( claypign )	/* (c) 1986 */
                /*TODO*/ //	DRIVER( chiller )	/* (c) 1986 */
                /*TODO*/ //	DRIVER( topsecex )	/* (c) 1986 */
                /*TODO*/ //	DRIVER( hitnmiss )	/* (c) 1987 */
                /*TODO*/ //	DRIVER( hitnmis2 )	/* (c) 1987 */
                /*TODO*/ //	DRIVER( whodunit )	/* (c) 1988 */
                /*TODO*/ //	DRIVER( showdown )	/* (c) 1988 */

                /* Atari vector games */
                /*TODO*/ //	DRIVER( asteroid )	/* (c) 1979 */
                /*TODO*/ //	DRIVER( asteroi1 )	/* no copyright notice */
                /*TODO*/ //	DRIVER( asteroib )	/* bootleg */
                /*TODO*/ //	DRIVER( astdelux )	/* (c) 1980 */
                /*TODO*/ //	DRIVER( astdelu1 )	/* (c) 1980 */
                /*TODO*/ //	DRIVER( bwidow )	/* (c) 1982 */
                /*TODO*/ //	DRIVER( bzone )		/* (c) 1980 */
                /*TODO*/ //	DRIVER( bzone2 )	/* (c) 1980 */
                /*TODO*/ //	DRIVER( gravitar )	/* (c) 1982 */
                /*TODO*/ //	DRIVER( gravitr2 )	/* (c) 1982 */
                /*TODO*/ //	DRIVER( llander )	/* no copyright notice */
                /*TODO*/ //	DRIVER( llander1 )	/* no copyright notice */
                /*TODO*/ //	DRIVER( redbaron )	/* (c) 1980 */
                /*TODO*/ //	DRIVER( spacduel )	/* (c) 1980 */
                /*TODO*/ //	DRIVER( tempest )	/* (c) 1980 */
                /*TODO*/ //	DRIVER( tempest1 )	/* (c) 1980 */
                /*TODO*/ //	DRIVER( tempest2 )	/* (c) 1980 */
                /*TODO*/ //	DRIVER( temptube )	/* hack */
                /*TODO*/ //	DRIVER( starwars )	/* (c) 1983 */
                /*TODO*/ //	DRIVER( starwar1 )	/* (c) 1983 */
                /*TODO*/ //	DRIVER( esb )		/* (c) 1985 */
                /*TODO*/ //	DRIVER( mhavoc )	/* (c) 1983 */
                /*TODO*/ //	DRIVER( mhavoc2 )	/* (c) 1983 */
                /*TODO*/ //	DRIVER( mhavocp )	/* (c) 1983 */
                /*TODO*/ //	DRIVER( mhavocrv )	/* hack */
                /*TODO*/ //	DRIVER( quantum )	/* (c) 1982 */	/* made by Gencomp */
                /*TODO*/ //	DRIVER( quantum1 )	/* (c) 1982 */	/* made by Gencomp */
                /*TODO*/ //	DRIVER( quantump )	/* (c) 1982 */	/* made by Gencomp */

                /* Atari b/w games */
                /*TODO*/ //	DRIVER( sprint1 )	/* no copyright notice */
                /*TODO*/ //	DRIVER( sprint2 )	/* no copyright notice */
                /*TODO*/ //	DRIVER( sbrkout )	/* no copyright notice */
                /*TODO*/ //	DRIVER( dominos )	/* no copyright notice */
                /*TODO*/ //	DRIVER( nitedrvr )	/* no copyright notice [1976] */
                /*TODO*/ //	DRIVER( bsktball )	/* no copyright notice */
                /*TODO*/ //	DRIVER( avalnche )	/* no copyright notice [1978] */
                /*TODO*/ //	DRIVER( subs )		/* no copyright notice [1976] */
                /*TODO*/ //	DRIVER( atarifb )	/* no copyright notice [1978] */
                /*TODO*/ //	DRIVER( atarifb1 )	/* no copyright notice [1978] */
                /*TODO*/ //	DRIVER( atarifb4 )	/* no copyright notice [1979] */
                /*TODO*/ //	DRIVER( abaseb )	/* no copyright notice [1979] */
                /*TODO*/ //	DRIVER( abaseb2 )	/* no copyright notice [1979] */
                /*TODO*/ //	DRIVER( soccer )	/* no copyright notice */
                /*TODO*/ //	DRIVER( canyon )	/* no copyright notice [1977] */
                /*TODO*/ //	DRIVER( canbprot )	/* no copyright notice [1977] */

                /* Atari "Centipede hardware" games */
                /*TODO*/ //	DRIVER( warlord )	/* (c) 1980 */

                /*TODO*/ //	DRIVER( milliped )	/* (c) 1982 */
                /*TODO*/ //	DRIVER( qwakprot )	/* (c) 1982 */

                /*TODO*/ //	DRIVER( arabian )	/* (c) 1983 Sun Electronics */
                /*TODO*/ //	DRIVER( arabiana )	/* (c) 1983 Atari */

                /* misc Atari games */
                /*TODO*/ //	DRIVER( liberatr )	/* (c) 1982 */
                /*TODO*/ //TESTDRIVER( liberat2 )

                /*TODO*/ //	DRIVER( cloak )		/* (c) 1983 */
                /*TODO*/ //	DRIVER( cloud9 )	/* (c) 1983 */
                /*TODO*/ //	DRIVER( jedi )		/* (c) 1984 */

                /* Atari System 1 games */
                /*TODO*/ //	DRIVER( marble )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( marble2 )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( marblea )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( peterpak )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( indytemp )	/* (c) 1985 */
                /*TODO*/ //	DRIVER( indytem2 )	/* (c) 1985 */
                /*TODO*/ //	DRIVER( indytem3 )	/* (c) 1985 */
                /*TODO*/ //	DRIVER( indytem4 )	/* (c) 1985 */
                /*TODO*/ //	DRIVER( roadrunn )	/* (c) 1985 */
                /*TODO*/ //	DRIVER( roadblst )	/* (c) 1986, 1987 */

                /* Atari System 2 games */
                /*TODO*/ //	DRIVER( paperboy )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( ssprint )	/* (c) 1986 */
                /*TODO*/ //	DRIVER( csprint )	/* (c) 1986 */
                /*TODO*/ //	DRIVER( 720 )		/* (c) 1986 */
                /*TODO*/ //	DRIVER( 720b )		/* (c) 1986 */
                /*TODO*/ //	DRIVER( apb )		/* (c) 1987 */
                /*TODO*/ //	DRIVER( apb2 )		/* (c) 1987 */

                /* later Atari games */
                /*TODO*/ //	DRIVER( gauntlet )	/* (c) 1985 */
                /*TODO*/ //	DRIVER( gauntir1 )	/* (c) 1985 */
                /*TODO*/ //	DRIVER( gauntir2 )	/* (c) 1985 */
                /*TODO*/ //	DRIVER( gaunt2p )	/* (c) 1985 */
                /*TODO*/ //	DRIVER( gaunt2 )	/* (c) 1986 */
                /*TODO*/ //	DRIVER( vindctr2 )	/* (c) 1988 */
                /*TODO*/ //	DRIVER( toobin )	/* (c) 1988 */
                /*TODO*/ //	DRIVER( toobin2 )	/* (c) 1988 */
                /*TODO*/ //	DRIVER( toobinp )	/* (c) 1988 */
                /*TODO*/ //	DRIVER( vindictr )	/* (c) 1988 */
                /*TODO*/ //	DRIVER( klax )		/* (c) 1989 */
                /*TODO*/ //	DRIVER( klax2 )		/* (c) 1989 */
                /*TODO*/ //	DRIVER( klax3 )		/* (c) 1989 */
                /*TODO*/ //	DRIVER( klaxj )		/* (c) 1989 (Japan) */
                /*TODO*/ //	DRIVER( blstroid )	/* (c) 1987 */
                /*TODO*/ //	DRIVER( blstroi2 )	/* (c) 1987 */
                /*TODO*/ //	DRIVER( xybots )	/* (c) 1987 */
                /*TODO*/ //	DRIVER( eprom )		/* (c) 1989 */
                /*TODO*/ //	DRIVER( eprom2 )	/* (c) 1989 */
                /*TODO*/ //	DRIVER( skullxbo )	/* (c) 1989 */
                /*TODO*/ //	DRIVER( skullxb2 )	/* (c) 1989 */
                /*TODO*/ //	DRIVER( badlands )	/* (c) 1989 */
                /*TODO*/ //	DRIVER( cyberbal )	/* (c) 1989 */
                /*TODO*/ //	DRIVER( cyberbt )	/* (c) 1989 */
                /*TODO*/ //	DRIVER( cyberb2p )	/* (c) 1989 */
                /*TODO*/ //	DRIVER( rampart )	/* (c) 1990 */
                /*TODO*/ //	DRIVER( ramprt2p )	/* (c) 1990 */
                /*TODO*/ //	DRIVER( rampartj )	/* (c) 1990 (Japan) */
                driver_shuuz, /* (c) 1990 */
                driver_shuuz2,/* (c) 1990 */
                /*TODO*/ //	DRIVER( hydra )		/* (c) 1990 */
                /*TODO*/ //	DRIVER( hydrap )	/* (c) 1990 */
                /*TODO*/ //	DRIVER( pitfight )	/* (c) 1990 */
                /*TODO*/ //	DRIVER( pitfigh3 )	/* (c) 1990 */
                /*TODO*/ //	DRIVER( thunderj )	/* (c) 1990 */
                /*TODO*/ //	DRIVER( batman )	/* (c) 1991 */
                /*TODO*/ //	DRIVER( relief )	/* (c) 1992 */
                /*TODO*/ //	DRIVER( relief2 )	/* (c) 1992 */
                /*TODO*/ //	DRIVER( offtwall )	/* (c) 1991 */
                /*TODO*/ //	DRIVER( offtwalc )	/* (c) 1991 */
                /*TODO*/ //	DRIVER( arcadecl )	/* (c) 1992 */
                /*TODO*/ //	DRIVER( sparkz )	/* (c) 1992 */

                /* later SNK games, each game can be identified by PCB code and ROM
	code, the ROM code is the same between versions, and usually based
	upon the Japanese title. */
                /*TODO*/ //	DRIVER( lasso )		/*       'WM' (c) 1982 */
                /*TODO*/ //	DRIVER( joyfulr )	/* A2001      (c) 1983 */
                /*TODO*/ //	DRIVER( mnchmobl )	/* A2001      (c) 1983 + Centuri license */

                /*TODO*/ //	DRIVER( hal21 )		/*            (c) 1985 */
                /*TODO*/ //	DRIVER( hal21j )	/*            (c) 1985 (Japan) */
                /*TODO*/ //	DRIVER( aso )		/*            (c) 1985 */

                driver_ikari3,
                driver_pow,
                driver_powj,
                driver_searchar,
                driver_sercharu,
                driver_streetsm,
                driver_streets1,
                driver_streetsj,
                /* Mechanized Attack   A8002 'MA' (c) 1989 */
                driver_prehisle,
                driver_prehislu,
                driver_gensitou,
                /* Beast Busters       A9003 'BB' (c) 1989 */
                /* SNK / Alpha 68K games */
                /*TODO*/ //TESTDRIVER( kyros )
                /*TODO*/ //TESTDRIVER( sstingry )
                /*TODO*/ //TESTDRIVER( paddlema )	/* Alpha-68K96I  'PM' (c) 1988 SNK */
                driver_timesold,
                driver_timesol1,
                driver_btlfield,
                driver_skysoldr,
                driver_goldmedl,
                /*TODO*/ //TESTDRIVER( goldmedb )	/* Alpha-68K96II bootleg */
                driver_skyadvnt,
                driver_gangwars,
                driver_gangwarb,
                driver_sbasebal,
                /* Technos games */
                /*TODO*/ //	DRIVER( bigprowr )	/* TA-0007 (c) 1983 */
                /*TODO*/ //	DRIVER( tagteam )	/* TA-0007 (c) 1983 + Data East license */
                /*TODO*/ //	DRIVER( ssozumo )	/* TA-0008 (c) 1984 */
                /* TA-0011 Dog Fight (Data East) / Batten O'hara no Sucha-Raka Kuuchuu Sen 1985 */
                /*TODO*/ //	DRIVER( battlane )	/* TA-???? (c) 1986 + Taito license */
                /*TODO*/ //	DRIVER( battlan2 )	/* TA-???? (c) 1986 + Taito license */
                /*TODO*/ //	DRIVER( battlan3 )	/* TA-???? (c) 1986 + Taito license */

                driver_ctribe,
                driver_ctribeb,
                /*TODO*/ //	DRIVER( blockout )	/* TA-0029 (c) 1989 + California Dreams */
                /*TODO*/ //	DRIVER( blckout2 )	/* TA-0029 (c) 1989 + California Dreams */
                driver_ddragon3,
                driver_ddrago3b,
                /* TA-0031 WWF Wrestlefest */
                /* Stern "Berzerk hardware" games */
                driver_berzerk,
                driver_berzerk1,
                driver_frenzy,
                /* GamePlan games */
                /*TODO*/ //	DRIVER( megatack )	/* (c) 1980 Centuri */
                /*TODO*/ //	DRIVER( killcom )	/* (c) 1980 Centuri */
                /*TODO*/ //	DRIVER( challeng )	/* (c) 1981 Centuri */
                /*TODO*/ //	DRIVER( kaos )		/* (c) 1981 */

                /* "stratovox hardware" games */
                /*TODO*/ //	DRIVER( route16 )	/* (c) 1981 Tehkan/Sun + Centuri license */
                /*TODO*/ //	DRIVER( route16b )	/* bootleg */
                /*TODO*/ //	DRIVER( stratvox )	/* Taito */
                /*TODO*/ //	DRIVER( stratvxb )	/* bootleg */
                /*TODO*/ //	DRIVER( speakres )	/* no copyright notice */

                /* Zaccaria games */
                /*TODO*/ //	DRIVER( monymony )	/* (c) 1983 */
                /*TODO*/ //	DRIVER( jackrabt )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( jackrab2 )	/* (c) 1984 */
                /*TODO*/ //	DRIVER( jackrabs )	/* (c) 1984 */

                /* UPL games */
                /*TODO*/ //	DRIVER( ninjakd2 )	/* (c) 1987 */
                /*TODO*/ //	DRIVER( ninjak2a )	/* (c) 1987 */
                /*TODO*/ //	DRIVER( ninjak2b )	/* (c) 1987 */
                /*TODO*/ //	DRIVER( rdaction )	/* (c) 1987 + World Games license */

                /* Williams/Midway TMS34010 games */
                /*TODO*/ //	DRIVER( narc )		/* (c) 1988 Williams */
                /*TODO*/ //TESTDRIVER( narc3 )		/* (c) 1988 Williams */
                /*TODO*/ //	DRIVER( trog )		/* (c) 1990 Midway */
                /*TODO*/ //	DRIVER( trog3 )		/* (c) 1990 Midway */
                /*TODO*/ //	DRIVER( trogp )		/* (c) 1990 Midway */
                /*TODO*/ //	DRIVER( smashtv )	/* (c) 1990 Williams */
                /*TODO*/ //	DRIVER( smashtv6 )	/* (c) 1990 Williams */
                /*TODO*/ //	DRIVER( smashtv5 )	/* (c) 1990 Williams */
                /*TODO*/ //	DRIVER( smashtv4 )	/* (c) 1990 Williams */
                /*TODO*/ //	DRIVER( hiimpact )	/* (c) 1990 Williams */
                /*TODO*/ //	DRIVER( shimpact )	/* (c) 1991 Midway */
                /*TODO*/ //	DRIVER( strkforc )	/* (c) 1991 Midway */
                /*TODO*/ //	DRIVER( mk )		/* (c) 1992 Midway */
                /*TODO*/ //	DRIVER( mkla1 )		/* (c) 1992 Midway */
                /*TODO*/ //	DRIVER( mkla2 )		/* (c) 1992 Midway */
                /*TODO*/ //	DRIVER( mkla3 )		/* (c) 1992 Midway */
                /*TODO*/ //	DRIVER( mkla4 )		/* (c) 1992 Midway */
                /*TODO*/ //	DRIVER( term2 )		/* (c) 1992 Midway */
                /*TODO*/ //	DRIVER( totcarn )	/* (c) 1992 Midway */
                /*TODO*/ //	DRIVER( totcarnp )	/* (c) 1992 Midway */
                /*TODO*/ //	DRIVER( mk2 )		/* (c) 1993 Midway */
                /*TODO*/ //	DRIVER( mk2r32 )	/* (c) 1993 Midway */
                /*TODO*/ //	DRIVER( mk2r14 )	/* (c) 1993 Midway */
                /*TODO*/ //	DRIVER( nbajam )	/* (c) 1993 Midway */
                /*TODO*/ //	DRIVER( nbajamr2 )	/* (c) 1993 Midway */
                /*TODO*/ //	DRIVER( nbajamte )	/* (c) 1994 Midway */

                /* Cinematronics vector games */
                /*TODO*/ //	DRIVER( spacewar )
                /*TODO*/ //	DRIVER( barrier )
                /*TODO*/ //	DRIVER( starcas )	/* (c) 1980 */
                /*TODO*/ //	DRIVER( starcas1 )	/* (c) 1980 */
                /*TODO*/ //	DRIVER( tailg )
                /*TODO*/ //	DRIVER( ripoff )
                /*TODO*/ //	DRIVER( armora )
                /*TODO*/ //	DRIVER( wotw )
                /*TODO*/ //	DRIVER( warrior )
                /*TODO*/ //	DRIVER( starhawk )
                /*TODO*/ //	DRIVER( solarq )	/* (c) 1981 */
                /*TODO*/ //	DRIVER( boxingb )	/* (c) 1981 */
                /*TODO*/ //	DRIVER( speedfrk )
                /*TODO*/ //	DRIVER( sundance )
                /*TODO*/ //	DRIVER( demon )		/* (c) 1982 Rock-ola */
                /* this one uses 68000+Z80 instead of the Cinematronics CPU */
                /*TODO*/ //	DRIVER( cchasm )
                /*TODO*/ //	DRIVER( cchasm1 )	/* (c) 1983 Cinematronics / GCE */

                /* "The Pit hardware" games */
                /*TODO*/ //	DRIVER( roundup )	/* (c) 1981 Amenip/Centuri */
                /*TODO*/ //	DRIVER( fitter )	/* (c) 1981 Taito */
                /*TODO*/ //	DRIVER( thepit )	/* (c) 1982 Centuri */
                /*TODO*/ //	DRIVER( intrepid )	/* (c) 1983 Nova Games Ltd. */
                /*TODO*/ //	DRIVER( intrepi2 )	/* (c) 1983 Nova Games Ltd. */
                /*TODO*/ //	DRIVER( portman )	/* (c) 1982 Nova Games Ltd. */
                /*TODO*/ //	DRIVER( suprmous )	/* (c) 1982 Taito */
                /*TODO*/ //	DRIVER( suprmou2 )	/* (c) 1982 Chu Co. Ltd. */
                /*TODO*/ //	DRIVER( machomou )	/* (c) 1982 Techstar */

                /* Seibu Denshi / Seibu Kaihatsu games */
                /*dynduke*/ driver_dynduke, /* (c) 1989 Seibu Kaihatsu + Fabtek license */
                /*dynduke*/ driver_dbldyn, /* (c) 1989 Seibu Kaihatsu + Fabtek license */
                /*TODO*/ //	DRIVER( dcon )		/* (c) 1992 Success */

                /* Tad games (Tad games run on Seibu hardware) */
                driver_bloodbro,
                driver_weststry,
                /* Jaleco games */
                /*TODO*/ //	DRIVER( exerion )	/* (c) 1983 Jaleco */
                /*TODO*/ //	DRIVER( exeriont )	/* (c) 1983 Jaleco + Taito America license */
                /*TODO*/ //	DRIVER( exerionb )	/* bootleg */

                /*TODO*/ //	DRIVER( pinbo )		/* (c) 1984 Jaleco */
                /*TODO*/ //	DRIVER( pinbos )	/* (c) 1985 Strike */

                /*TODO*/ //	DRIVER( ginganin )	/* (c) 1987 Jaleco */
                /*TODO*/ //	DRIVER( cischeat )	/* (c) 1990 Jaleco */
                /*TODO*/ //	DRIVER( f1gpstar )	/* (c) 1991 Jaleco */

                /* Jaleco Mega System 1 games */
                /*TODO*/ //	DRIVER( lomakai )	/* (c) 1988 (World) */
                /*TODO*/ //	DRIVER( makaiden )	/* (c) 1988 (Japan) */
                /*TODO*/ //	DRIVER( p47 )		/* (c) 1988 */
                /*TODO*/ //	DRIVER( p47j )		/* (c) 1988 (Japan) */
                /*TODO*/ //	DRIVER( kickoff )	/* (c) 1988 (Japan) */
                /*TODO*/ //	DRIVER( tshingen )	/* (c) 1988 (Japan) */
                /*TODO*/ //	DRIVER( astyanax )	/* (c) 1989 */
                /*TODO*/ //	DRIVER( lordofk )	/* (c) 1989 (Japan) */
                /*TODO*/ //	DRIVER( hachoo )	/* (c) 1989 */
                /*TODO*/ //	DRIVER( plusalph )	/* (c) 1989 */
                /*TODO*/ //	DRIVER( stdragon )	/* (c) 1989 */
                /*TODO*/ //	DRIVER( iganinju )	/* (c) 1989 (Japan) */
                /*TODO*/ //	DRIVER( rodland )	/* (c) 1990 */
                /*TODO*/ //	DRIVER( rodlandj )	/* (c) 1990 (Japan) */
                /*TODO*/ //	DRIVER( 64street )	/* (c) 1991 */
                /*TODO*/ //	DRIVER( 64streej )	/* (c) 1991 (Japan) */
                /*TODO*/ //	DRIVER( edf )		/* (c) 1991 */
                /*TODO*/ //	DRIVER( avspirit )	/* (c) 1991 */
                /*TODO*/ //	DRIVER( phantasm )	/* (c) 1991 (Japan) */
                /*TODO*/ //	DRIVER( bigstrik )	/* (c) 1992 */
                /*TODO*/ //	DRIVER( chimerab )	/* (c) 1993 */
                /*TODO*/ //	DRIVER( cybattlr )	/* (c) 1993 */
                /*TODO*/ //	DRIVER( peekaboo )	/* (c) 1993 */
                /*TODO*/ //	DRIVER( soldamj )	/* (c) 1992 (Japan) */

                /* Video System Co. games */
                driver_pspikes, /* (c) 1991 */
                driver_svolly91, /* (c) 1991 */
                driver_turbofrc, /* (c) 1991 */
                driver_aerofgt, /* (c) 1992 */
                driver_aerofgtb, /* (c) 1992 */
                driver_aerofgtc, /* (c) 1992 */
                /*TODO*/ //TESTDRIVER( unkvsys )

                /* Vastar was made by Orca, but when it was finished, Orca had already bankrupted. */
                /* So they sold this game as "Made by Sesame Japan" because they couldn't use */
                /* the name "Orca" */
                /*TODO*/ //	DRIVER( vastar )	/* (c) 1983 Sesame Japan */
                /*TODO*/ //	DRIVER( vastar2 )	/* (c) 1983 Sesame Japan */

                /* Gaelco games */
                driver_splash, /* (c) 1992 Gaelco */
                /* Kaneko "AX System" games */
                driver_berlwall,
                driver_berlwalt,
                driver_gtmr,
                driver_gtmre,
                /*TODO*/ //TESTDRIVER( gtmr2 )
                /*TODO*/ //TESTDRIVER( shogwarr )

                /* other Kaneko games */
                /*TODO*/ //	DRIVER( spacefb )	/* (c) [1980?] Nintendo */
                /*TODO*/ //	DRIVER( spacefbg )	/* 834-0031 (c) 1980 Gremlin */
                /*TODO*/ //	DRIVER( spacefbb )	/* bootleg */
                /*TODO*/ //	DRIVER( spacebrd )	/* bootleg */
                /*TODO*/ //	DRIVER( spacedem )	/* (c) 1980 Nintendo / Fortrek */

                /*TODO*/ //	DRIVER( omegrace )	/* (c) 1981 Midway */
                /*TODO*/ //	DRIVER( dday )		/* (c) 1982 Olympia */
                /*TODO*/ //	DRIVER( ddayc )		/* (c) 1982 Olympia + Centuri license */

                /*TODO*/ //	DRIVER( leprechn )	/* (c) 1982 Tong Electronic */
                /*TODO*/ //	DRIVER( potogold )	/* (c) 1982 Tong Electronic */

                /*TODO*/ //	DRIVER( redalert )	/* (c) 1981 Irem (GDI game) */
                /*TODO*/ //	DRIVER( irobot )	/* (c) 1983 Atari */
                /*TODO*/ //	DRIVER( spiders )	/* (c) 1981 Sigma Ent. Inc. */
                /*TODO*/ //	DRIVER( spiders2 )	/* (c) 1981 Sigma Ent. Inc. */
                /*TODO*/ //	DRIVER( stactics )	/* [1981 Sega] */
                /*TODO*/ //	DRIVER( exterm )	/* (c) 1989 Premier Technology - a Gottlieb game */
                /*TODO*/ //	DRIVER( sharkatt )	/* (c) Pacific Novelty */
                /*TODO*/ //	DRIVER( zerozone )	/* (c) 1993 Comad */
                /*TODO*/ //	DRIVER( exctsccr )	/* (c) 1983 Alpha Denshi Co. */
                /*TODO*/ //	DRIVER( exctscca )	/* (c) 1983 Alpha Denshi Co. */
                /*TODO*/ //	DRIVER( exctsccb )	/* bootleg */
                /*TODO*/ //	DRIVER( exctscc2 )

                /*TODO*/ //	DRIVER( ambush )	/* (c) 1983 Nippon Amuse Co-Ltd */
                /*TODO*/ //	DRIVER( starcrus )	/* [1977 Ramtek] */
                /*TODO*/ //	DRIVER( shanghai )	/* (c) 1988 Sun Electronics */

                /*TODO*/ //TESTDRIVER( dlair )
                /*TODO*/ //	DRIVER( meteor )	/* (c) 1981 Venture Line */

                /*TODO*/ //	DRIVER( aztarac )	/* (c) 1983 Centuri (vector game) */
                /*TODO*/ //	DRIVER( mole )		/* (c) 1982 Yachiyo Electronics, Ltd. */
                /*TODO*/ //	DRIVER( gotya )		/* (c) 1981 Game-A-Tron */

                /* Neo Geo games */
                /* the four digits number is the game ID stored at address 0x0108 of the program ROM */
                /*TODO*/ //	DRIVER( nam1975 )	/* 0001 (c) 1990 SNK */
                /*TODO*/ //	DRIVER( bstars )	/* 0002 (c) 1990 SNK */
                /*TODO*/ //	DRIVER( tpgolf )	/* 0003 (c) 1990 SNK */
                /*TODO*/ //	DRIVER( mahretsu )	/* 0004 (c) 1990 SNK */
                /*TODO*/ //	DRIVER( maglord )	/* 0005 (c) 1990 Alpha Denshi Co */
                /*TODO*/ //	DRIVER( maglordh )	/* 0005 (c) 1990 Alpha Denshi Co */
                /*TODO*/ //	DRIVER( ridhero )	/* 0006 (c) 1990 SNK */
                /*TODO*/ //	DRIVER( alpham2 )	/* 0007 (c) 1991 SNK */
                /* 0008 */
                /*TODO*/ //	DRIVER( ncombat )	/* 0009 (c) 1990 Alpha Denshi Co */
                /*TODO*/ //	DRIVER( cyberlip )	/* 0010 (c) 1990 SNK */
                /*TODO*/ //	DRIVER( superspy )	/* 0011 (c) 1990 SNK */
                /* 0012 */
                /* 0013 */
                /*TODO*/ //	DRIVER( mutnat )	/* 0014 (c) 1992 SNK */
                /* 0015 */
                /*TODO*/ //	DRIVER( kotm )		/* 0016 (c) 1991 SNK */
                /*TODO*/ //	DRIVER( sengoku )	/* 0017 (c) 1991 SNK */
                /*TODO*/ //	DRIVER( sengokh )	/* 0017 (c) 1991 SNK */
                /*TODO*/ //	DRIVER( burningf )	/* 0018 (c) 1991 SNK */
                /*TODO*/ //	DRIVER( burningh )	/* 0018 (c) 1991 SNK */
                /*TODO*/ //	DRIVER( lbowling )	/* 0019 (c) 1990 SNK */
                /*TODO*/ //	DRIVER( gpilots )	/* 0020 (c) 1991 SNK */
                /*TODO*/ //	DRIVER( joyjoy )	/* 0021 (c) 1990 SNK */
                /*TODO*/ //	DRIVER( bjourney )	/* 0022 (c) 1990 Alpha Denshi Co */
                /*TODO*/ //	DRIVER( quizdais )	/* 0023 (c) 1991 SNK */
                /*TODO*/ //	DRIVER( lresort )	/* 0024 (c) 1992 SNK */
                /*TODO*/ //	DRIVER( eightman )	/* 0025 (c) 1991 SNK / Pallas */
                /* 0026 Fun Fun Brothers - prototype? */
                /*TODO*/ //	DRIVER( minasan )	/* 0027 (c) 1990 Monolith Corp. */
                /* 0028 */
                /*TODO*/ //	DRIVER( legendos )	/* 0029 (c) 1991 SNK */
                /*TODO*/ //	DRIVER( 2020bb )	/* 0030 (c) 1991 SNK / Pallas */
                /*TODO*/ //	DRIVER( 2020bbh )	/* 0030 (c) 1991 SNK / Pallas */
                /*TODO*/ //	DRIVER( socbrawl )	/* 0031 (c) 1991 SNK */
                /*TODO*/ //	DRIVER( roboarmy )	/* 0032 (c) 1991 SNK */
                /*TODO*/ //	DRIVER( fatfury1 )	/* 0033 (c) 1991 SNK */
                /*TODO*/ //	DRIVER( fbfrenzy )	/* 0034 (c) 1992 SNK */
                /* 0035 */
                /*TODO*/ //	DRIVER( bakatono )	/* 0036 (c) 1991 Monolith Corp. */
                /*TODO*/ //	DRIVER( crsword )	/* 0037 (c) 1991 Alpha Denshi Co */
                /*TODO*/ //	DRIVER( trally )	/* 0038 (c) 1991 Alpha Denshi Co */
                /*TODO*/ //	DRIVER( kotm2 )		/* 0039 (c) 1992 SNK */
                /*TODO*/ //	DRIVER( sengoku2 )	/* 0040 (c) 1993 SNK */
                /*TODO*/ //	DRIVER( bstars2 )	/* 0041 (c) 1992 SNK */
                /*TODO*/ //	DRIVER( quizdai2 )	/* 0042 (c) 1992 SNK */
                /*TODO*/ //	DRIVER( 3countb )	/* 0043 (c) 1993 SNK */
                /*TODO*/ //	DRIVER( aof )		/* 0044 (c) 1992 SNK */
                /*TODO*/ //	DRIVER( samsho )	/* 0045 (c) 1993 SNK */
                /*TODO*/ //	DRIVER( tophuntr )	/* 0046 (c) 1994 SNK */
                /*TODO*/ //	DRIVER( fatfury2 )	/* 0047 (c) 1992 SNK */
                /*TODO*/ //	DRIVER( janshin )	/* 0048 (c) 1994 Aicom */
                /*TODO*/ //	DRIVER( androdun )	/* 0049 (c) 1992 Visco */
                /*TODO*/ //	DRIVER( ncommand )	/* 0050 (c) 1992 Alpha Denshi Co */
                /*TODO*/ //	DRIVER( viewpoin )	/* 0051 (c) 1992 Sammy */
                /*TODO*/ //	DRIVER( ssideki )	/* 0052 (c) 1992 SNK */
                /*TODO*/ //	DRIVER( wh1 )		/* 0053 (c) 1992 Alpha Denshi Co */
                /* 0054 Crossed Swords 2 (CD only) */
                driver_kof94, /* 0055 (c) 1994 SNK */
                driver_aof2, /* 0056 (c) 1994 SNK */
                /*TODO*/ //	DRIVER( wh2 )		/* 0057 (c) 1993 ADK */
                /*TODO*/ //	DRIVER( fatfursp )	/* 0058 (c) 1993 SNK */
                /*TODO*/ //	DRIVER( savagere )	/* 0059 (c) 1995 SNK */
                /*TODO*/ //	DRIVER( fightfev )	/* 0060 (c) 1994 Viccom */
                /*TODO*/ //	DRIVER( ssideki2 )	/* 0061 (c) 1994 SNK */
                /*TODO*/ //	DRIVER( spinmast )	/* 0062 (c) 1993 Data East Corporation */
                /*TODO*/ //	DRIVER( samsho2 )	/* 0063 (c) 1994 SNK */
                /*TODO*/ //	DRIVER( wh2j )		/* 0064 (c) 1994 ADK / SNK */
                /*TODO*/ //	DRIVER( wjammers )	/* 0065 (c) 1994 Data East Corporation */
                /*TODO*/ //	DRIVER( karnovr )	/* 0066 (c) 1994 Data East Corporation */
                /*TODO*/ //	DRIVER( gururin )	/* 0067 (c) 1994 Face */
                /*TODO*/ //	DRIVER( pspikes2 )	/* 0068 (c) 1994 Video System Co. */
                /*TODO*/ //	DRIVER( fatfury3 )	/* 0069 (c) 1995 SNK */
                /* 0070 */
                /* 0071 */
                /* 0072 */
                /*TODO*/ //	DRIVER( panicbom )	/* 0073 (c) 1994 Eighting / Hudson */
                /*TODO*/ //	DRIVER( aodk )		/* 0074 (c) 1994 ADK / SNK */
                /*TODO*/ //	DRIVER( sonicwi2 )	/* 0075 (c) 1994 Video System Co. */
                /*TODO*/ //	DRIVER( zedblade )	/* 0076 (c) 1994 NMK */
                /* 0077 */
                /*TODO*/ //	DRIVER( galaxyfg )	/* 0078 (c) 1995 Sunsoft */
                /*TODO*/ //	DRIVER( strhoop )	/* 0079 (c) 1994 Data East Corporation */
                /*TODO*/ //	DRIVER( quizkof )	/* 0080 (c) 1995 Saurus */
                /*TODO*/ //	DRIVER( ssideki3 )	/* 0081 (c) 1995 SNK */
                /*TODO*/ //	DRIVER( doubledr )	/* 0082 (c) 1995 Technos */
                /*TODO*/ //	DRIVER( pbobble )	/* 0083 (c) 1994 Taito */
                /*TODO*/ //	DRIVER( kof95 )		/* 0084 (c) 1995 SNK */
                /* 0085 Shinsetsu Samurai Spirits Bushidoretsuden / Samurai Shodown RPG (CD only) */
                /*TODO*/ //	DRIVER( tws96 )		/* 0086 (c) 1996 Tecmo */
                /*TODO*/ //	DRIVER( samsho3 )	/* 0087 (c) 1995 SNK */
                /*TODO*/ //	DRIVER( stakwin )	/* 0088 (c) 1995 Saurus */
                /*TODO*/ //	DRIVER( pulstar )	/* 0089 (c) 1995 Aicom */
                /*TODO*/ //	DRIVER( whp )		/* 0090 (c) 1995 ADK / SNK */
                /* 0091 */
                /*TODO*/ //	DRIVER( kabukikl )	/* 0092 (c) 1995 Hudson */
                /*TODO*/ //	DRIVER( neobombe )	/* 0093 (c) 1997 Hudson */
                /*TODO*/ //	DRIVER( gowcaizr )	/* 0094 (c) 1995 Technos */
                /*TODO*/ //	DRIVER( rbff1 )		/* 0095 (c) 1995 SNK */
                /*TODO*/ //	DRIVER( aof3 )		/* 0096 (c) 1996 SNK */
                /*TODO*/ //	DRIVER( sonicwi3 )	/* 0097 (c) 1995 Video System Co. */
                /* 0098 Idol Mahjong - final romance 2 (CD only? not confirmed, MVS might exist) */
                /* 0099 */
                /*TODO*/ //	DRIVER( turfmast )	/* 0200 (c) 1996 Nazca */
                driver_mslug, /* 0201 (c) 1996 Nazca */
                /*TODO*/ //	DRIVER( puzzledp )	/* 0202 (c) 1995 Taito (Visco license) */
                /*TODO*/ //	DRIVER( mosyougi )	/* 0203 (c) 1995 ADK / SNK */
                /* 0204 ADK World (CD only) */
                /* 0205 Neo-Geo CD Special (CD only) */
                /*TODO*/ //	DRIVER( marukodq )	/* 0206 (c) 1995 Takara */
                /*TODO*/ //	DRIVER( neomrdo )	/* 0207 (c) 1996 Visco */
                /*TODO*/ //	DRIVER( sdodgeb )	/* 0208 (c) 1996 Technos */
                /*TODO*/ //	DRIVER( goalx3 )	/* 0209 (c) 1995 Visco */
                /* 0210 */
                /* 0211 Oshidashi Zintrick (CD only? not confirmed, MVS might exist) */
                /*TODO*/ //	DRIVER( overtop )	/* 0212 (c) 1996 ADK */
                /*TODO*/ //	DRIVER( neodrift )	/* 0213 (c) 1996 Visco */
                /*TODO*/ //	DRIVER( kof96 )		/* 0214 (c) 1996 SNK */
                /*TODO*/ //	DRIVER( ssideki4 )	/* 0215 (c) 1996 SNK */
                /*TODO*/ //	DRIVER( kizuna )	/* 0216 (c) 1996 SNK */
                /*TODO*/ //	DRIVER( ninjamas )	/* 0217 (c) 1996 ADK / SNK */
                /*TODO*/ //	DRIVER( ragnagrd )	/* 0218 (c) 1996 Saurus */
                /*TODO*/ //	DRIVER( pgoal )		/* 0219 (c) 1996 Saurus */
                /* 0220 Choutetsu Brikin'ger - iron clad (CD only? not confirmed, MVS might exist) */
                /*TODO*/ //	DRIVER( magdrop2 )	/* 0221 (c) 1996 Data East Corporation */
                /*TODO*/ //	DRIVER( samsho4 )	/* 0222 (c) 1996 SNK */
                /*TODO*/ //	DRIVER( rbffspec )	/* 0223 (c) 1996 SNK */
                /*TODO*/ //	DRIVER( twinspri )	/* 0224 (c) 1996 ADK */
                /*TODO*/ //	DRIVER( wakuwak7 )	/* 0225 (c) 1996 Sunsoft */
                /* 0226 */
                /*TODO*/ //	DRIVER( stakwin2 )	/* 0227 (c) 1996 Saurus */
                /* 0228 */
                /* 0229 King of Fighters '96 CD Collection (CD only) */
                /*TODO*/ //	DRIVER( breakers )	/* 0230 (c) 1996 Visco */
                /*TODO*/ //	DRIVER( miexchng )	/* 0231 (c) 1997 Face */
                /*TODO*/ //	DRIVER( kof97 )		/* 0232 (c) 1997 SNK */
                /*TODO*/ //	DRIVER( magdrop3 )	/* 0233 (c) 1997 Data East Corporation */
                /*TODO*/ //	DRIVER( lastblad )	/* 0234 (c) 1997 SNK */
                /*TODO*/ //	DRIVER( puzzldpr )	/* 0235 (c) 1997 Taito (Visco license) */
                /*TODO*/ //	DRIVER( irrmaze )	/* 0236 (c) 1997 SNK / Saurus */
                /*TODO*/ //	DRIVER( popbounc )	/* 0237 (c) 1997 Video System Co. */
                /*TODO*/ //	DRIVER( shocktro )	/* 0238 (c) 1997 Saurus */
                /*TODO*/ //	DRIVER( blazstar )	/* 0239 (c) 1998 Yumekobo */
                /*TODO*/ //	DRIVER( rbff2 )		/* 0240 (c) 1998 SNK */
                /*TODO*/ //	DRIVER( mslug2 )	/* 0241 (c) 1998 SNK */
                /*TODO*/ //	DRIVER( kof98 )		/* 0242 (c) 1998 SNK */
                /*TODO*/ //	DRIVER( lastbld2 )	/* 0243 (c) 1998 SNK */
                /*TODO*/ //	DRIVER( neocup98 )	/* 0244 (c) 1998 SNK */
                /*TODO*/ //	DRIVER( breakrev )	/* 0245 (c) 1998 Visco */
                /*TODO*/ //	DRIVER( shocktr2 )	/* 0246 (c) 1998 Saurus */
                /*TODO*/ //	DRIVER( flipshot )	/* 0247 (c) 1998 Visco */
                /*TODO*/ //TESTDRIVER( pbobbl2n )	/* 0248 (c) 1999 Taito (SNK license) */
                /*TODO*/ //TESTDRIVER( ctomaday )	/* 0249 (c) 1999 Visco */
                /*TODO*/ //TESTDRIVER( mslugx )	/* 0250 (c) 1999 SNK */
                /*TODO*/ //TESTDRIVER( kof99 )		/* 0251 (c) 1999 SNK */
                /*TODO*/ //TESTDRIVER( garou )		/* 0253 (c) 1999 SNK */

                /**
                 * **
                 * 0.37b7 games
                 *
                 */
                /*jrpacman*/ driver_jrpacman,
                /*macross */ driver_macross,
                /*cop01   */ driver_cop01,
                /*cop01   */ driver_cop01a,
                /*zodiack */ driver_bounty,
                /*galpanic */ driver_fantasia,
                /*galpanic */ driver_newfant,
                /*galpanic */ driver_missw96,
                /*galspnbl */ driver_hotpinbl,
                /*galspnbl */ driver_galspnbl,
                null /* end of array */};
}
