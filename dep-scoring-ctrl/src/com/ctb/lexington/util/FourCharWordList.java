package com.ctb.lexington.util;

/*
 * FourCharWordList.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 */


/**
 * <code>CTBConstants</code> defines constants used throughout the application.
 *	Taken from: http://kufacts.cc.ukans.edu/ftp/pub/history/Europe/Medieval/aids/latwords.html
 *
 *	Latin words truncated to 4 chars and filtered to be unique.
 *
 * @author  Chris Ulbrich
 * @version $Id$
 */
public final class FourCharWordList extends Object
{
   public static String[] LATIN_DICTIONARY = {
"abal","abav","abba","abdi","abdo","abeo","aber","abho","abic","abie","abig","abiu","ablu","abne","abno","abnu","abol",
"abom","abor","abro","abru","absc","abse","absi","abso","absq","abst","absu","abun","abut","abys","acce","acci","accl",
"acco","accr","accu","acer","acid","acie","acqu","acri","acsi","acti","adam","adap","adau","addo","adeo","adep","adfi",
"adha","adhu","adic","adim","adin","adip","adit","adiu","adji","adju","admi","admo","adni","adnu","adol","adop","ador",
"adpr","adse","adst","adsu","adue","adul","adve","advo","aege","aegr","aene","aequ","aera","aeru","aest","aeta","aeth",
"aevu","affa","affe","affi","affl","affo","ager","agge","aggr","agit","agme","agna","agni","agno","agnu","agon","agra",
"agre","agri","aiun","alac","alar","alat","alau","alba","albe","albu","alce","alcy","alea","ales","alga","alge","algi",
"algo","alia","alib","alic","alie","alig","alim","alio","alip","aliq","alit","aliu","alle","allo","alma","almu","alnu",
"alqu","alte","alti","alto","altu","alum","alve","amar","amba","ambi","ambu","amen","amic","amis","amit","amni","amoe",
"amor","ampl","anci","andr","anet","anfr","ange","angl","ango","angu","anhe","anim","anne","anno","annu","ansa","anse",
"ante","anti","antr","anxi","aper","apex","appa","appe","appl","appo","appr","apto","aptu","apud","aqua","aqui","arat",
"arbi","arbo","arbu","arca","arce","arch","arcu","arde","ardo","ardu","arge","argu","aris","arma","armi","armo","arro",
"arti","arto","artu","asci","aspe","aspi","aspo","asse","assi","assu","astr","astu","ater","atqu","atro","atte","atto",
"atti","attr","auar","auct","auda","aude","audi","aufe","auge","augm","aula","aura","auri","auru","aust","ausu","autu",
"auxi","avar","aveh","avel","aver","avoc","avun","avus","acca","baci","bacu","baiu","baju","bala","balb","bale","bali",
"ball","baln","balo","bara","barb","bard","bari","baro","basi","batt","beat","bell","bene","beni","bibo","bipe","blan",
"blas","blat","boat","bona","bonu","brac","brav","brev","brit","buci","bull","cacu","cada","cado","caed","cael","caet",
"cala","calc","cale","cali","call","calt","calu","calv","calx","cami","camp","camu","cana","canc","cand","cani","cano",
"cant","capi","capr","capt","capu","carb","carc","card","care","cari","carm","carn","caro","carp","carr","caru","casa",
"cass","cast","casu","cate","catt","caud","caus","caut","cava","cave","cavu","cedo","cele","cell","celo","cels","cena",
"ceno","cens","cent","cere","cern","cert","cerv","cess","cete","chal","char","chir","chor","ciba","cibo","cibu","cimi",
"cine","cini","circ","cita","citi","cito","civi","clad","clam","clan","clar","clau","clem","clib","clim","clyp","coac",
"coad","coae","coan","coar","coep","coer","cogi","cogn","cogo","coha","cohi","coho","coit","coll","colo","comb","comi",
"comm","comp","cona","conc","cond","conf","cong","coni","conj","conn","cono","conq","cons","cont","conv","coop","copi",
"copu","corb","cori","corn","coro","corp","corr","cort","coru","cour","cout","crap","cras","creb","cred","crem","creo",
"crep","cres","cret","crib","crim","crin","spar","cris","croc","crot","cruc","crue","crum","cruo","crus","cryp","crys",
"cuba","cubi","cubo","cuiu","culm","culp","cult","cumu","cuna","cunc","cune","cupi","cupp","cupr","cura","curi","curo",
"curr","curs","curt","curt","curv","cusp","cust","cycl","daci","damn","dape","dapi","daps","deal","debe","debi","debr",
"dece","deci","decl","deco","decr","decu","dede","dedi","dedo","defa","defe","defi","defl","defu","dege","degu","dein",
"dele","deli","delu","deme","demi","demo","demu","dene","deni","deno","dens","denu","deor","depa","depe","depo","depr",
"depu","dere","deri","deru","desa","desc","dese","desi","deso","desp","dest","desu","dete","deti","detr","deus","devi",
"devo","dext","diab","diad","diap","diar","diat","didi","diff","dige","digl","dign","digr","dila","dile",
"dilg","dili","dilu","dimi","dire","diri","diru","disc","disi","disp","pens","disr","dist","dita","diti","dito",
"diur","diut","dive","divi","divo","divu","doct","dole","dolo","dolu","dome","domi","domu","dona","done","dono","donu",
"dorm","doxa","duca","duco","dudu","dulc","dumt","ebul","ecce","ecqu","edic","edit","edoc","educ","edul","edur","effe",
"effi","effl","effo","effr","effu","egel","egen","egeo","egre","ejul","elab","elat","elec","eleg","elem","elid","elig",
"eloq","eluc","eluo","eman","emen","emer","emic","emin","emio","emir","emov","empt","emun","enim","enit","ensi",
"enuc","enum","enut","epis","epit","epop","epos","epul","equa","eque","equi","equu","erad","erec","erep","erga","ergo",
"erig","eril","erip","erog","erra","erro","erub","eruc","erud","erum","eruo","erus","ervu","esca","escu","esit","esse",
"esur","eten","etho","etia","etsi","evac","evad","evag","eval","evan","evas","eveh","evel","even","ever","eves","evid",
"evig","evin","evis","evit","evoc","evol","evom","evul","exae","exag","exal","exan","exar","exas","exau","exce","exci",
"excl","exco","excr","excu","exec","exem","exeo","exer","exes","exha","exhe","exhi","exho","exig","exil","exim","exin",
"exis","exit","exor","exos","expa","expe","expi","expl","expo","expr","expu","exqu","exse","exsi","exso","exsp","exst",
"exsu","exte","exti","exto","extr","extu","exub","exul","exun","exuo","exus","exuv","faba","fabe","fabr","fabu","face",
"faci","fact","facu","faen","faex","fall","fals","falx","fama","fame","fami","famo","famu","fana","fanu","farc","fari",
"farr","fast","fate","fatu","faut","fave","favi","fecu","fefe","feli","femi","fene","fera",
"fere","feri","ferm","fero","ferr","feru","ferv","fess","fest","fete","fict","ficu","fide","fidu","fien","fili","fimu",
"fine","fing","fini","firm","flag","flam","flan","flat","fleb","flec","fleo","flet","flex","floc","flor","flos","fluc",
"flui","flum","fluo","fodi","foed","foet","foli","foll","fome","fons","fora","forc","ford","fore","forf","fori","form",
"forn","foro","fort","foru","foss","fove","frag","fran","fran","frat","frau","fren","freq","fret","frig","friv","fron",
"frug","frum","fruo","frus","frut","fuga","fugi","fugo","fult","fumi","fumo","fund","fune","fung","funi","fura",
"furi","furo","furs","fusu","futu","gala","galb","gale","gall","gand","garr","garu","gaud","gaui","gaza","geli",
"gelo","gelu","gemb","gemi","gemm","gemo","gene","geni","gens","genu","germ","gero","geru","gest","ging","glac","glad",
"gleb","glor","glut","gnar","grad","gram","gran","gras","grat","grav","greg","gres","grex","gube","gurg","gust","gutt",
"habe","habi","hact","haer","hanc","haru","hasn","hast","haud","hebe","helc","heln","here","here","heus","hila","hinc",
"hodi","hoie","holo","hone","hono","hora","horr","hort","horu","hosp","host","huic","huiu","huma","humi","humo","humu",
"hunc","iace","iaci","iact","iacu","iamd","iani","ianu","ictu","idci","iden","ideo","ieiu","ient","igit","igna","igne",
"igni","igno","ilic","illa","ille","illi","illo","illu","imag","imbe","imbi","imbr","imbu","imco","imit","imma","imme",
"immi","immo","immu","impe","impi","impl","impo","impr","impu","inae","inan","inas","inca","ince","inch","inci","incl",
"inco","incr","incu","inda","inde","indi","indo","indu","ined","inef","inen","ineo","iner","inex","infa","infe","infi",
"infl","info","infr","infu","inge","ingl","ingr","ingu","inha","inhi","inho","inhu","inib","inim","iniq","init","iniu",
"inju","inno","innu","inol","inop","inqu","inre","inri","inru","insa","insc","inse","insi","inso","insp","insq","inst",
"insu","insu","inte","inti","into","intr","intu","inul","inun","inva","inve","invi","invo","iocu","ipse","irat","iron",
"irra","irre","irri","irru","itaq","item","iter","iube","iucu","iude","iudi","iugo","iugu","iume","iunc","iuni","iura",
"iure","iurg","iuri","iuro","iuss","iust","iuve","iuvo","iuxt","jaci","jact","jacu","jubi","jucu","judi","juge","jugi",
"jugu","jume","jura","jurg","just","juve","juxt","laba","labe","labi","labo","labr","lace","lacr","lact","lacu","laet",
"laga","lama","lamb","lame","lami","lamp","lanc","lane","lani","lanx","laqu","larg","lasc","lase","late","lati","latr",
"latu","laud","laud","laur","laus","laut","lauv","lava","lavo","laxe","laxo","laxu","lect","lega","lege","legi","lego",
"legu","lemi","lemm","lemu","leni","leno","lens","lent","leod","lepi","lepo","lepu","leta","leth","leti","leto","letu",
"leva","levi","levo","libe","libi","libr","lice","lici","liga","lign","ligo","lima","lime","limi","limu","ling","lini",
"lino","liqu","liti","lito","livo","loci","loco","locu","logi","long","loqu","lori","loru","lubr","luce","luci","lucr",
"luct","lucu","ludi","ludo","ludu","luge","lugo","lume","lumi","luna","lupu","luto","luxu","mace","maci","macr",
"mact","macu","made","madi","maga","magi","magn","magu","maie","maio","majo","mala","male","mali","malm","malo","manc",
"mand","mane","mani","mano","mans","mara","marc","mari","marm","maro","mart","mate","matr","maxi","medi","meli","mell",
"memb","memo","mend","mens","mere","meri","mess","mest","meti","metu","meus","mica","mich","mico","mihi","mili","mill",
"mina","mini","mino","minu","mira","mire","miro","miru","mise","misf","miss","mite","miti","mitt","mode","modi","modo",
"modu","moen","moer","moes","mogu","mole","moli","moll","mona","mona","mone","moni","mons","mont","monu","mora","more",
"mori","moro","mors","mort","moru","mucr","mugi","mulc","muli","mult","mund","muni","munu","murm","muru","muta","muto",
"mutu","naev","namu","narr","nati","natu","nava","navi","nebu","nece","necn","neco","nect","nefa","negl","nego","nemo",
"nemu","nequ","nesc","nich","nido","nige","nihi","nimi","nisi","nisu","nite","niti","nito","nive","nive","nixo","nixu",
"nobi","noce","noer","nole","nolo","nome","nomi","nond","nonn","nonu","norm","nort","nosc","nota","noto","nous","nove",
"novi","novo","novu","noxa","null","nume","numq","nunc","nunq","nunt","nupe","nupt","nusq","nuto","nutr","nutu","obdo",
"obdu","obed","obex","obju","obli","obni","obno","obnu","obru","obsc","obse","obsi","obst","obsu","obte","obti","obtu",
"obvi","obvo","occa","occi","occl","occu","ociu","ocul","odio","odiu","oest","offa","offe","offi","olim","omit",
"omni","oner","onus","oper","opes","opin","opis","opor","oppi","oppo","oppr","oppu","opta","opti","opto","opul","opus",
"orat","orbi","orbu","orca","orcu","ordi","ordo","orgi","oric","orie","orie","orig","orio","oriu","orna","orno","ornu",
"orsa","orsu","oryx","oryz","osti","otiu","ovan","paci","pact","paec","paen","paga","page","pagu","pala","pale","pali",
"pall","palm","palo","palp","palu","pand","pang","pani","pann","pant","papa","papi","papp","papu","para","parc","pard",
"pare","pari","paro","pars","part","paru","parv","pasc","pass","past","pate","pati","patr","patu","pauc","paul","paup",
"pave","pavi","pavo","pecc","pect","pecu","pede","pedi","peio","pela","pell","pelo","pelv","pend","pene","peni","penn",
"penu","pepl","pera","perc","perd","pere","perf","perg","peri","perj","perl","perm","pern","pero","perp","perq","pers",
"pert","peru","perv","pess","pest","peti","peto","petr","petu","phal","phar","phil","phit","phoe","phth","phyl","phys",
"piac","pica","pice","pict","picu","piet","pign","pilo","pilu","pine","ping","pipi","piru","pius","plac","plag","plan",
"plas","plat","plau","pleb","plec","plen","pler","plic","plor","plui","plum","pluo","plur","plus","pneu","poci","poda",
"poen","poet","pole","poll","polu","pomu","pond","pons","popo","popu","porr","port","posc","posi","poss","post","pote",
"poti","prac","prae","pran","prat","prav","prec","preh","prem","pren","preo","prep","pres","pret","prev","prex","prim",
"prin","prio","pris","priu","priv","prob","proc","prod","proe","prof","prog","proh","proi","prol","prom","pron","prop",
"pror","pros","prot","rote","prou","prov","prox","prud","prur","psal","pube","publ","puch","pude","pudi","pudo","puel",
"puer","puga","pugn","pulc","pule","pull","pulm","pulp","puls","pulv","punc","pung","puni","pupa","pupi","pupp","purg",
"purp","puru","pusi","puta","pute","puto","putu","quad","quae","qual","quam","quan","quap","quar","quas","quat","quem",
"quen","queo","quer","ques","quia","quib","quic","quid","quie","quil","quin","quip","quir","quis","quod","quom","quon",
"quoq","quor","quos","quot","quov","rabi","radi","rapi","rapt","rare","raru","rati","re v","rebe","rebo","rece","reci",
"reco","recr","rect","recu","reda","redd","rede","redi","redo","redu","refe","refi","refo","refu","rega","rege","regi",
"regn","rego","rela","rele","reli","relu","rema","reme","remi","remo","remu","reni","reno","renu","reor","repe","repl",
"repo","repr","repu","requ","resa","rese","resi","reso","resp","rest","resu","reta","rete","reti","retr","retu","reus",
"reve","revo","rhet","ridi","rigi","rigo","rimo","risu","rite","ritu","rixo","robu","rogo","rota","roto","rotu","rubo",
"rudi","rudo","rufu","rugi","ruin","rume","rumi","rumo","rump","runa","runc","rust","ruta","ruti","sabb","sacc","sace",
"sacr","saep","saet","saev","saga","sagi","salt","salu","salv","sanc","sane","sann","sano","sapi","sapo","sarc","sarc",
"sata","sate","sati","sato","satu","saxu","scab","scac","scae","scal","scam","scan","scap","scel","scep","sche","scho",
"scie","scil","scin","scio","scit","sciu","scop","scri","scru","scul","scut","sece","secl","seco","secr","sect","secu",
"sede","sedi","sedo","sedu","sege","segn","seme","semi","semo","semp","sene","seni","sens","sent","seor","sepe","sepo",
"sept","sepu","sequ","sere","seri","serm","sero","serv","sese","sesq","sess","seve","sicc","sicl","sicu","side",
"sidu","sigi","sign","sile","sili","silv","simi","simp","simu","sinc","sine","sing","sino","sinu","siqu","sist","siti",
"situ","sive","soce","soci","soco","socu","soda","sode","sola","sole","soli","soll","solo","solu","solv","some","somn",
"soni","sono","soph","sopo","sord","sors","sort","sosp","spat","spec","spen","sper","spes","spir","sple","spol","spon",
"spur","squa","stab","stab","stat","stel","stem","ster","stil","stim","stip","stol","stom","stra","stre","stri","stru",
"suad","suas","suav","suba","subc","subd","sube","subi","subj","subl","subn","subo","subr","subs","subt","subv","sudo",
"suff","sugg","sulu","summ","sumo","sump","supe","supp","supr","surc","surg","surr","surs","susc","susi","susp","sust",
"susu","suus","tabe","tabi","tabu","tace","tact","taed","taet","tala","tale","tali","talu","tamd","tame","tami","tand",
"tang","tanq","tant","tard","taru","tect","tegm","tego","tell","telu","teme","temp","temp","tena","tend","tene","teno",
"tent","tenu","tepe","tepi","tere","terg","term","tero","terr","ters","tert","test","texo","text","thal","thea","thec",
"them","theo","ther","thes","thor","thym","timi","timo","tinn","tiro","tole","toll","tons","torm","torn","torp","torq",
"torr","tort","toti","toto","totu","trab","trac","trad","trah","trai","tran","treb","tred","trel","trem","trep","trev",
"trib","tric","trid","tril","trim","trin","trip","tris","trit","triu","truc","trud","trun","trut","trux","tubi","tueo",
"tumb","tume","tumi","tumo","tumu","tunc","turb","turg","turm","turp","tuss","tuta","tute","tuti","tuto","tutu","typu",
"typi","tyra","uale","uber","uera","ulci","ulla","ullu","ulte","ulto","ultr","ulul","umbr","umqu","unci","unda","unde",
"undo","univ","unus","uran","urba","urbs","ured","urge","usit","usqu","usur","usus","uter","utic","util","utin","utiq",
"utor","utri","utro","utru","uxor","acil","vaco","vado","vadu","vafr","vagi","vagu","vald","vale","vali","vall","vapu",
"vari","vast","vate","vect","vege","vegr","vehe","vehi","veho","vele","veli","vell","velo","velu","vena","vend","vend",
"vene","veni","veno","vent","venu","vepr","verb","verb","verc","vere","verg","veri","verm","vern","vern","vero","vers",
"vert","veru","vesc","vesi","vesp","vest","vete","vetu","vexa","vexi","vexo","viat","vica","vice","vici","vict","vicu",
"vide","vidu","vieo","vige","vigi","vigo","vili","vill","vinc","vind","vine","vini","vinu","viol","vipe","virg","viri",
"virt","viru","visc","visi","visp","visu","vita","vito","vitu","viva","vivi","vivo","vobi","voci","voco","vola","vole",
"voli","volo","volu","vome","vomi","vora","voro","vove","vulc","vulg","vuln","vulp","vult","xiph","ymbe","zelu","zeph"
};

public static String[] VULGAR = {
"DAMN","DIC","FART","LUST","SEX","SHIT","SUC","FUC","TIT","ASS","PRIC","COC","DORK","FAG","CUNT","BOOB","LICK"
};

public static String[] NEGATIVE = {
"DUMB","FAIL","LOSE","FLUNK"
};

}
/**
 * $Log$
 * Revision 1.1  2007/01/30 01:31:46  ncohen
 * port scoring to 4.x platform
 *
 * Revision 1.1  2006/02/23 20:48:29  ncohen
 * create new module for legacy OAS app
 *
 * Revision 1.6  2002/10/16 05:50:54  kgawetsk
 * Added VULGAR, NEGATIVE arrays.
 *
 */