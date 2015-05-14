CREATE OR REPLACE PACKAGE IREAD3_PRESCHEDULING_2015_SUMR IS

  -- Author  : TCS Dev Offshore
  -- Created : 02/06/2015
  -- Purpose : To schedule students into test sessions in bulk for IREAD3

  --FUNCTION generateAccessCode RETURN VARCHAR2;

  -- 02/06/2015 - updated words lists to remove long words
  WORDS1 CONSTANT VARCHAR2(32726) := 'aback,abacus,abalone,abate,abbey,abbot,abdicate,aberrant,abet,abetted,abetting,abeyance,abeyant,abide,abject,able,ablution,abnormal,aboard,abo
de,abolish,aborning,abound,about,above,abrasion,abrasive,abridge,abroad,abrogate,abrupt,abscissa,absence,absent,absentee,absentia,absolute,absolve,abs
orb,abstain,abstract,abstruse,abundant,abysmal,abyss,academe,academia,academic,academy,accede,accent,accept,acceptor,access,accident,acclaim,accolade,
accord,accost,account,accredit,accrual,accrue,accuracy,accurate,accustom,ace,acerbic,acerbity,acetate,acetic,acetone,ache,achieve,aching,acidic,acme,a
corn,acoustic,acquaint,acquire,acquit,acre,acreage,acrimony,acrobat,acronym,across,acrylic,act,activate,activation,activism,actor,actress,actual,actua
rial,actuate,acuity,acumen,acute,acyclic,ad,adage,adagio,adamant,adapt,adaptation,adaptive,add,added,addend,addenda,addendum,addition,additional,addit
ive,address,addressee,adduce,adenine,adenoma,adenosine,adept,adequacy,adequate,adhere,adherent,adhesion,adhesive,adiabatic,adjacent,adject,adjectival,
adjective,adjoin,adjoint,adjourn,adjudge,adjudicate,adjunct,adjust,adjutant,administer,administrable,administrate,admiral,admiralty,admiration,admire,
admissible,admission,admit,admittance,admitted,admitting,admonish,admonition,ado,adobe,adolescent,adoptive,adore,adorn,adrenal,adrenali
ne,adrift,adroit,adsorb,adulate,advance,advent,adverb,adverse,advert,advice,advise,advisee,advisor,advisory,advocacy,advocate,aegis,aeolian,aerate,aer
ial,aerobic,aerosol,aesthete,afar,affable,affect,afferent,affine,affinity,affirm,affix,afflict,afford,affront,afield,afire,aflame,afloat,afoot,afraid,
afresh,aft,again,against,agate,age,agenda,agent,aggrieve,agile,aging,agitate,ago,agone,agony,agrarian,agree,agreed,agreeing,agrimony,ague,ahead,aide,a
il,aileron,aim,air,airborne,aircraft,airdrop,airedale,airfare,airfield,airflow,airfoil,airframe,airlift,airline,airlock,airmail,airman,airmen,
airpark,airplane,airport,airspace,airspeed,airstrip,airtight,airway,airy,aisle,ajar,akin,alacrity,alarm,alb,albacore,albeit,album,albumin,alchemy,alco
ve,aldehyde,aleph,alert,alfalfa,alfresco,alga,algae,algal,alias,align,alike,aliquot,alive,alkali,alkaline,alkaloid,alkane,alkene,all,allege,allegory,a
llegro,allele,allemand,allergic,allergy,alley,alleyway,alliance,allied,allocate,allot,allotted,allow,alloy,allspice,allude,allure,allusion,allusive,al
luvial,alluvium,ally,allyl,almanac,almond,almost,aloe,aloft,aloha,alone,along,aloof,aloud,alp,alpha,alphabet,alpine,already,also,alterate,although,alt
itude,alto,altruism,altruist,alum,alumna,alumnae,alumni,alumnus,always,alyssum,am,amalgam,amateur,amaze,amber,ambiance,ambient,ambition,amble,am
bling,ambrosia,ambulant,ambulate,amend,amethyst,amicable,amid,amide,amidst,amino,amiss,amity,ammeter,ammonia,ammonium,amnesia,amoeba,amoebae,among,amo
ngst,amount,amp,amperage,ampere,ample,amplify,amply,amulet,amuse,an,anaconda,anaglyph,anagram,analogue,analogy,analyses,analysis,analyst,analytic,anat
omic,anatomy,ancestor,ancestry,anchor,anchovy,ancient,and,anecdote,anemone,anew,angelic,angle,angling,angstrom,angular,aniline,animal,animate,an
ion,anionic,ankle,annex,annotate,announce,annoy,annoyance,annual,annuity,annunciate,anode,anodic,anomalous,anomaly,anonymity,anonymous,another,a
nswer,ant,antacid,antagonism,antagonist,antagonistic,antarctic,anteater,antebellum,antecedent,antedate,antelope,antenna,antennae,anterior,anteroom,ant
hem,anther,anthology,anthropogenic,anthropology,anthropomorphic,anthropomorphism,antic,anticipate,anticipatory,antigen,antimony,antipasto,antipathy,an
tiperspirant,antipode,antipodean,antipodes,antiquarian,antiquary,antiquated,antique,antiquity,antithetic,antler,antonym,anvil,anxiety,anxious,any,anyb
ody,anyhow,anyone,anyplace,anything,anyway,anywhere,aorta,apace,apart,ape,aperiodic,aperture,apex,aphid,aphorism,apiece,aplomb,apocalypse,apocalyptic,
apocryphal,apogee,apologetic,apologia,apology,apostle,apostolic,apostrophe,apothecary,apothegm,apotheosis,appall,appanage,apparatus,apparel,apparent,a
pparition,appeal,appear,appease,append,appendix,appetite,applaud,applause,apple,applied,applique,apply,appoint,apposite,appraise,apprise,approach,appr
oval,approve,apricot,apron,apropos,apt,aptitude,aqua,aquarium,aquatic,aqueduct,aqueous,arachnid,arbiter,arboreal,arc,arcade,arcane,arch,archaic,archai
sm,archery,arching,arcing,arctic,ardency,ardent,arduous,are,area,areaway,arena,argon,argue,argument,arid,arise,arisen,ark,arm,armada,armament,armature
,armchair,armful,armload,armoire,army,aroma,aromatic,arose,around,arpeggio,arraign,arrange,array,arrival,arrive,arrogate,arrow,arroyo,art,art
erial,artery,artful,article,artifact,artifice,artisan,artistry,artwork,as,ascend,ascent,ascribe,aseptic,ash,ashame,ashamed,ashen,ashore,aside,ask,aska
nce,askew,asleep,aspect,aspen,asperity,aspirate,aspire,aspirin,assemble,assent,assert,assessor,asset,assign,assist,assume,assure,asteria,asterisk,aste
roid,asthma,astonish,astound,astral,astride,astute,asunder,at,atavism,athle,athletic,athwart,atlantic,atlas,atom,atomic,atonal,atone,atop,atrium,a
trophic,atrophy,attach,attache,attain,attempt,attend,attendee,attest,attic,attire,attitude,attorney,attract,attune,auburn,auction,audacity,audible,aud
ience,audio,audit,audition,auditor,auditory,auger,augment,august,auk,aunt,auntie,aura,aural,auric,aurochs,aurora,auspices,austere,author,auto,autocrat
,automate,autonomy,autumn,autumnal,avail,avarice,avenue,aver,average,averred,averring,averse,aviary,aviate,avid,avionic,avocado,await,awake,awaken,awa
rd,aware,awash,away,awe,awesome,awhile,awkward,awl,awn,awoke,awry,ax,axe,axes,axial,axiology,axiom,axis,axle,axon,azalea,azimuth,azimuthal,azure,babbl
e,baboon,baby,babyhood,babysat,babysit,babysitter,babysitting,baccalaureate,bacilli,bacillus,back,backboard,backbone,backdrop,backfill,backgammon,back
ground,backhand,backlash,backorder,backpack,backplane,backstage,backstitch,backstop,backtrack,backup,backward,backwood,backyard,bacteria,bacteri
al,bacterium,bade,badge,badinage,badland,badminton,baffle,bag,bagatelle,bagel,baggage,bagpipe,bait,bake,bakery,balance,balcony,bald,baldy,bale,baleen,
baleful,balk,balky,ball,ballad,ballast,ballerina,ballet,balletic,ballfield,balloon,ballot,ballroom,balm,balmy,balsa,balsam,balustrade,bamboo,ban
,banana,band,bandage,bandgap,bandit,bandpass,bandstand,bandstop,bandwagon,bandwidth,bandy,bane,baneberry,baneful,bangle,banish,banister,banjo,bank,ban
krupt,banquet,bantam,banter,barb,barbaric,barbecue,barbell,barber,barberry,bard,bargain,barge,baritone,barium,bark,barley,barn,barnacle,barny
ard,baron,baroness,baronet,baronial,barony,baroque,barrack,barrage,barre,barrel,barren,barrette,barrier,barrow,barter,basal,basalt,base,baseball,baseb
and,baseline,baseman,basemen,bash,bashaw,bashful,basic,basil,basilar,basilisk,basin,basis,bask,basket,bass,bassi,bassinet,basso,basswood,baste,bastion
,bat,batch,bate,bateau,bathos,bathrobe,bathroom,bathtub,batik,baton,batt,batten,battery,battle,batwing,bauble,baud,bauxite,bawl,bay,bayed,b
ayonet,bayou,bazaar,be,beach,beacon,bead,beadle,beady,beak,beam,bean,bear,beard,bearish,beatific,beatify,beautify,beauty,becalm,became,because,beck,be
cket,beckon,becloud,become,bed,bedazzle,bedfast,bedim,bedimmed,bedlam,bedpost,bedrock,bedroom,bedside,bedstraw,bedtime,bee,beebread,bee
ch,beef,beehive,been,beep,beet,beetle,befall,befallen,befell,befit,befog,before,befuddle,beg,began,beggary,begging,begin,beginner,begonia,begot
ten,begrudge,beguile,begun,behalf,behave,beheld,behest,behind,behold,beige,being,bel,belate,belfry,belie,belief,belies,believe,bell,bellboy,belle,bell
hop,bellman,bellmen,bellow,bellum,belong,belove,below,belt,belying,bemadden,beman,bemoan,bemuse,bench,bend,beneath,benefice,benefit,benight,beni
gn,bent,benzene,bequeath,bequest,beret,berg,bergamot,beribbon,beriberi,berne,berry,berth,beryl,beseech,beset,beside,besiege,besmirch,besotte,d,bespeak
,bespoke,best,bestir,bestow,bestowal,bet,beta,betatron,betroth,bettor,between,betwixt,bevel,beverage,bevy,bewail,beware,bewilder,bey,beyond,bias,biaxi
al,bib,bicep,biceps,bicker,bicycle,bid,biddable,bidden,bide,biennial,biennium,bifocal,big,bike,bilayer,bilinear,bilk,bill,billet,billfold,billiard,bil
lion,billow,bimodal,bin,binary,binaural,bind,bindery,bindle,binomial,biota,biotic,biotite,biplane,bipolar,birch,bird,birdbath,birdie,birdlike,b
irdseed,birth,birthday,biscuit,bisect,bishop,bismuth,bison,bisque,bistable,bistate,bit,bite,bitten,bitumen,bitwise,bivalve,bivouac,biz,bizarre,blab,bl
acken,blackout,blanch,bland,blandish,blank,blanket,blare,blast,blastula,blazon,bleach,bleak,bleary,bleat,blemish,blend,blight,blimp,blink,blip,b
liss,blissful,blister,blithe,blitz,blizzard,block,blockade,blockage,blond,blonde,bloom,blossom,blot,blouse,blue,blueback,blueberry,bluebill,bluebird,b
luebonnet,bluebush,bluefish,bluegill,bluet,bluff,bluish,blunder,blunt,blur,blurb,blurry,blurt,blush,bluster,blustery,boa,boar,board,boardinghouse,boas
t,boastful,boat,boathouse,boatload,boatman,boatmen,boatswain,boatyard,bobcat,body,bodybuild,bodybuilder,bodybuilding,bodyguard,bog,bogey,boggle,boil,b
oisterous,bold,boldface,bolometer,bolster,bolt,bombard,bombast,bon,bona,bonanza,bond,bondsman,bondsmen,bonfire,bongo,bonnet,bonus,book,bookbind,b
ookcase,bookend,bookish,bookkeep,booklet,boorish,boost,boot,booth,bop,borate,borax,border,born,borne,borough,borrow,boson,bosonic,boss,botanic,botanis
t,botany,botfly,both,bottle,bouffant,bough,bought,boulder,boule,bounce,bound,boundary,bounty,bouquet,bourn,bout,boutique,bovine,bow,bowfin,bowie,bowl,
bowline,bowman,bowmen,box,boxcar,boxwood,boxy,boy,boyar,boycott,boyhood,brace,bracelet,bracken,bracket,brackish,bract,brad,brae,brag,bragging,braid,br
ain,brainy,brake,brakeman,bramble,bran,branch,brand,brandish,brant,brash,brass,brassy,bravado,brave,bravery,bravo,bravura,brawl,bray,brazier,breach,br
ead,breadth,break,breakage,breakoff,breakup,bream,breath,breathe,breathy,breccia,breech,breed,breeze,breezy,brethren,breve,brevet,brevity,briar,brick,
brickbat,bricklay,bridal,bride,bridge,bridle,brief,brig,brigade,bright,brighten,brim,brimful,brindle,brine,bring,brink,briny,brisk,bristle,brittle,bro
ach,broaden,brocade,broccoli,brochure,brockle,broil,broke,broken,bromide,bromine,bronchi,bronchus,bronco,bronze,bronzy,brood,broody,brook,broom,broth,
brother,brought,brouhaha,brow,brownie,brownish,browse,bruise,bruit,brunch,brunette,brunt,brush,brushy,brusque,bryozoa,bubble,bucket,buckeye,buck
le,buddy,budge,budget,buffalo,buffet,bug,bugging,buggy,bugle,build,buildup,built,builtin,bulb,bulblet,bulge,bulk,bulkhead,bulky,bulldog,bulldoze,bulle
t,bulletin,bullfrog,bullseye,bulrush,bulwark,bumble,bump,bun,bunch,bundle,bungalow,bungle,bunk,bunny,bunt,buoy,buoyant,burbank,burdock,bureau,buret,bu
rette,burgeon,burgess,burgher,burglar,burl,burlap,burley,burly,burn,burnish,burnt,burro,burrow,burst,bursty,bury,bus,busboy,buses,bushel,business,bust
le,busy,but,butane,butler,buttery,button,buy,buyer,buzz,buzzer,buzzing,buzzsaw,buzzword,buzzy,by,bye,bygone,bylaw,byline,bypass,bypath,byroad,byway,by
word';

  WORDS2 CONSTANT VARCHAR2(32726) := 'cab,cabana,cabaret,cabbage,cabin,cabinet,cable,cacao,cachalot,cache,cackle,cacti,cactus,caddy,cadent,cadenza,cadet,cadmium,cadre,cafe,cage,cagey,cajo
le,cake,calamity,calamus,calcify,calcine,calcite,calcium,calculi,calculus,caldera,calendar,calf,calfskin,caliber,calibre,calico,caliper,caliph,call,ca
ller,calliope,callous,callus,calm,caloric,calorie,calumny,calve,calypso,camber,cambric,camel,camellia,cameo,camera,camilla,camp,campaign,campfire,camp
ion,campsite,campus,can,canal,canary,candela,candid,candle,candy,cane,canine,canister,cannel,cannery,cannibal,cannon,canny,canoe,canon,canopy,canst,ca
nt,canteen,canticle,cantle,canto,canton,cantor,canvas,canvass,canyon,cap,capacity,cape,capella,caper,capita,capital,capitol,capo,caprice,capsize,capst
an,capstone,capsule,captain,caption,captious,captive,captor,capture,capybara,car,carabao,caramel,caravan,caraway,carbide,carbine,carbon,carbonic,carbo
nyl,carboxy,card,cardamom,cardiac,cardinal,cardiod,cardioid,care,careen,career,carefree,careful,caret,careworn,carfare,cargo,cargoes,caribou,carload,c
arne,carney,carnival,carob,carol,carp,carpet,carport,carrel,carriage,carrot,carry,cart,carte,carton,cartoon,carve,carven,caryatid,cascade,casc
ara,case,casebook,casein,casework,cash,cashew,cashier,cashmere,cask,cassette,cassock,cast,castanet,caste,casteth,castle,castor,casual,casualty,cat,cat
alpa,catalyst,catapult,cataract,catawba,catbird,catch,catchup,catchy,category,catenate,cater,catfish,cathedra,cathode,cation,cationic,catkin,catlike,c
atnip,catsup,cattail,cattle,caucus,caught,cauldron,caulk,causal,causate,cause,caustic,caution,cautious,cavalier,cavalry,cave,caveat,caveman,cavemen,ca
vern,caviar,cavil,cavitate,cavort,caw,cayenne,cease,cedar,cede,cedilla,ceil,celerity,celery,celesta,cell,cellar,cellular,cement,cemetery,censor,censur
e,census,cent,centaur,central,centric,centrist,centroid,centum,century,ceramic,ceramium,cereal,cerebral,ceremony,cereus,cerise,cerium,certain,certify,
cesium,cession,chagrin,chain,chair,chairman,chairmen,chaise,chalet,chalice,chalk,chalky,chamber,chamfer,chamois,champ,champion,chance,chancel,chancery
,chancy,chandler,change,channel,chanson,chant,chantey,chantry,chap,chapel,chaperon,chaplain,chapter,charcoal,chard,charge,chariot,charisma,charity,cha
rm,chart,chase,chasm,chassis,chaste,chastise,chat,chateau,chaw,cheap,check,checkout,checksum,checkup,cheek,cheer,cheerful,cheery,cheese,cheesy,cheetah
,chef,chelate,chemic,chemise,chemist,chenille,cherish,cherry,chert,cherub,cherubim,chess,chest,chestnut,chevron,chevy,chew,chicken,chicory,chide,chief
,chiefdom,chiffon,child,childish,children,chili,chill,chilly,chime,chimera,chimeric,chimney,chin,chinch,chine,chip,chipmunk,chirp,chisel,chiton,chival
ry,chive,chlorate,chloride,chlorine,chock,choice,choir,cholera,choose,choosy,chop,choppy,choral,chorale,chord,chordal,chordata,chordate,chore,chorine,
chortle,chorus,chose,chosen,chow,chowder,chromate,chrome,chromic,chromium,chronic,chuckle,chuff,chunk,churn,chute,chutney,cicada,cider,cilia,ciliate,c
inch,cinder,cinema,cinnabar,cinnamon,cipher,circa,circle,circlet,circuit,circular,circus,cistern,citadel,citation,cite,citizen,citrate,citric,citron,c
itrus,city,citywide,civet,civic,civil,civilian,clad,claim,claimant,clam,clamber,clammy,clamp,clan,claret,clarify,clarinet,clarity,clash,clasp,class,cl
assic,classify,classy,clatter,clattery,clause,claw,clay,clean,cleanse,cleanup,clear,cleat,cleft,clement,clench,cleric,clerk,clever,cliche,click,client
,cliff,climate,climatic,climb,clime,clinch,cling,clinging,clinic,clink,clip,clique,cloak,clock,clog,clogging,clone,clonic,close,closet,closeup,closure
,clot,cloth,clothe,clothier,cloture,cloud,cloudy,clout,clove,cloven,clown,cloy,club,clubroom,cluck,clue,clump,clumsy,clung,cluster,clutch,clutter,coac
h,coachman,coachmen,coal,coalesce,coarse,coarsen,coast,coastal,coat,coattail,coauthor,coax,coaxial,cobalt,cobble,cobra,cobweb,cockpit,cocoa,coconut,co
coon,cod,coda,code,codeword,codfish,codicil,codify,codomain,coed,coeditor,coequal,coerce,coercion,coercive,coexist,cofactor,coffee,coffer,cog,cogent,c
ogitate,cognate,cohere,coherent,cohesion,cohesive,cohort,cohosh,coiffure,coil,coin,coinage,coincide,colander,cold,coleus,coliform,coliseum,collage,col
lagen,collapse,collar,collard,collate,collect,college,collet,collide,collie,colloquy,collude,colonel,colonial,colonist,colony,colorate,colossal,coloss
i,colossus,colt,coltish,column,columnar,colza,comb,combine,comeback,comedian,comedy,comet,cometary,comfort,comic,comma,command,commend,comment,commerc
e,commit,common,commute,compact,company,compare,compass,compel,compete,compile,complain,compleat,complete,complex,compline,comply,comport,compose,comp
ote,compound,compress,comprise,compute,comrade,concave,conceal,concede,conceit,concept,concern,concert,concerti,concerto,concise,conclave,conclu
de,concoct,concord,concrete,concur,condemn,condense,condone,conduce,conduct,conduit,cone,confect,confer,conferee,confess,confide,confine,confirm,confo
cal,conform,confound,confrere,confront,confute,congeal,congener,congest,congress,conic,conifer,conjunct,conjure,connect,connive,connote,conquer,conque
st,consent,conserve,consider,consign,consist,console,conspire,constant,construe,consul,consular,consult,consume,contact,contain,contend,content,contex
t,continua,continue,continuo,contort,contour,contract,contrary,contrast,contribu,contrite,contrive,control,convect,convene,convent,converge,converse,c
onvert,convex,convey,conveyor,convict,convince,convoke,convolve,convoy,convulse,cook,cookbook,cookery,cookie,coolant,coop,coot,cop,cope,copious,coplan
ar,copperas,coppery,copra,coprinus,copy,copybook,coquina,coral,corbel,cord,cordage,cordial,cordite,cordon,corduroy,core,cork,corn,cornea,corn
et,cornish,cornmeal,corny,corona,coronary,coronate,coroner,coronet,corpora,corporal,corps,corpsman,corpsmen,corpus,corral,correct,corridor,corrode,cor
sage,cortege,cortex,cortical,corundum,corvette,cosmetic,cosmic,cosmos,cost,costume,cosy,cot,cotman,cotta,cottage,cotton,couch,cougar,cough,could,coulo
mb,council,counsel,count,country,county,coup,coupe,couple,coupon,courage,courier,course,court,courtesy,courtier,couscous,cousin,covalent,covary,cove,c
ovenant,cover,coverage,coverall,coverlet,cowbell,cowbird,cowboy,cowgirl,cowhand,cowherd,cowhide,cowl,coworker,cowpox,coy,coyote,cozy,crab,crabmeat,cra
dle,craft,crafty,cramp,crane,crania,cranium,cranny,crass,crate,crater,cravat,crave,craw,crawl,crayfish,crayon,craze,crazy,creak,creaky,crease,create,c
reating,creature,creche,credent,credenza,credible,credit,creditor,credo,creed,creedal,creek,creosote,crepe,crept,crescent,cress,crest,crevice,crew,cre
wcut,crewel,crewman,crewmen,crib,cricket,cried,crime,crimson,crinkle,crisp,criss,criteria,critic,critique,critter,croak,crochet,croft,crone,crony,croo
k,croon,crop,croquet,crossarm,crossbar,crossbow,crosscut,crossway,crouch,crow,crowd,crowfoot,crown,crucial,crucible,cruise,crumb,crumble,crumple
,crunch,crupper,crusade,crush,crust,cry,cryptic,crystal,cub,cube,cubic,cuckoo,cucumber,cud,cuddle,cuddly,cudgel,cue,cuff,cufflink,cuisine,culinary,cul
l,culpable,culprit,cultural,culture,culvert,cumin,cumulate,cumulus,cunning,cup,cupboard,cupful,cupidity,cupric,cuprous,cur,curate,curb,curbside,curd,c
urdle,cure,curfew,curia,curie,curio,curious,curium,curl,curlew,curlicue,currant,current,curry,cursive,cursor,cursory,curt,curtail,curtain,curtsey,curv
e,cushion,cusp,custody,custom,cut,cutback,cute,cutesy,cutlass,cutler,cutlet,cut,cutout,cutover,cutset,cutworm,cycad,cycle,cyclic,cyclist,cyclone,cylin
der,cynic,cypress,cyst,cysteine,cytology,dab,dabble,dactyl,dactylic,dad,daddy,daffodil,daffy,dagger,dahlia,dainty,dairy,dairyman,dairymen,daisy,dale,d
ally,damp,dampen,dance,dandy,dapper,dapple,dare,dark,darken,darkle,darling,dart,dash,data,database,date,dateline,dater,datum,daub,daughter,daunt,dauph
in,dauphine,davit,dawn,day,daybreak,daydream,daylight,daytime,daze,dazzle,deadline,deadlock,deal,dealt,dean,dear,dearie,dearth,debar,debate,debater,de
bility,debit,debonair,debrief,debris,debt,debtor,debug,debugged,debunk,debut,decade,decadent,decal,decant,decay,decedent,deceit,decent,decibel,decide,
decile,decimal,decipher,decision,decisive,deck,declaim,declare,decline,decode,decor,decorate,decorous,decorum,decouple,decoy,decrease,decree,decry,ded
icate,deduce,deed,deem,deep,deepen,deer,deerskin,deface,default,defeat,defector,defend,defer,deferent,deferred,define,definite,deflect,deforest,deft,d
egree,delay,delegate,delicacy,delicate,delight,delimit,delirium,deliver,delivery,dell,delphine,delta,deltoid,delude,deluge,delusion,delusive,deluxe,de
lve,demand,demark,demitted,demo,den,denature,dendrite,denizen,denote,dense,dent,dental,denture,depart,depend,depict,deplete,deploy,depose,deposit,depo
t,depth,depute,deputy,derby,derivate,derive,descant,descend,descent,describe,desert,deserve,design,desist,desk,despite,dessert,destine,destiny
,detach,detail,detain,detect,detector,deter,detour,deuce,develop,device,devote,devotee,devotion,dew,dewar,dewdrop,dextrose,dextrous,diadem,diagnose,di
agonal,diagram,dial,dialect,dialogue,diameter,diamond,diary,diatom,diatomic,diatonic,dice,did,didactic,diesel,diet,dietary,dietetic,differ,diffract,di
ffuse,dig,digest,digging,digit,digital,dignify,dignity,digram,digress,dihedral,dilemma,diligent,dill,diluent,dilute,dilution,dim,dime,dimethyl,diminis
h,dimple,dine,dinosaur,diode,diopter,diorama,diorite,dioxide,dip,diploid,diploidy,diplo,diplomat,direct,director,disburse,disc,discern,disco,discreet,
discrete,discus,discuss,dish,dishes,dishevel,disjunct,disk,dispel,dispense,disperse,dissuade,distaff,distal,distant,distinct,district,dither,ditto,div
e,diverge,diverse,divert,divest,divide,dividend,division,divisor,divulge,do,doberman,dock,docket,dockside,dockyard,doctor,doctoral,doctrine,document,d
odge,doe,dog,doghouse,dogwood,doldrum,doldrums,dole,doleful,doll,dollar,dollop,dolly,dolomite,dolphin,domain,dome,domestic,domicile,domino,donate,done
,door,doorbell,doorkeep,doorknob,doorman,doormen,doorstep,doorway,dormant,dossier,dot,dote,double,doublet,doubt,doubtful,dough,doughnut,dour,douse,dov
e,dovetail,dowel,down,downplay,downpour,downtown,downward,downwind,dowry,doze,dozen,drab,draft,drag,dragging,dragnet,dragon,dragoon,drain,drainage,dra
ke,dram,dramatic,drape,drapery,drastic,draw,drawback,drawl,drawn,dream,dreamt,dreamy,dredge,dreg,drench,dress,dressy,drew,drib,dried,drier,drift
,drill,drink,drive,driven,driveway,drizzle,drizzly,droll,drone,drop,droplet,dross,drought,drove,drowse,drowsy,drub,druid,drum,drumhead,drumlin,dry,dry
ad,dual,dualism,dub,ducat,duchess,duck,duckling,duct,ductile,ductwork,dud,due,duel,duet,duff,duffel,dug,dugout,duke,dukedom,dulcet,dull,dully,dulse,du
ly,dune,dunk,duopoly,duplex,durable,durance,duration,during,dusk,dust,dustbin,dusty,dutchess,dutiable,dutiful,duty,dwindle,dyad,dyadic,dye,dyeing,dyer
,dynamic,dynamism,dynasty,each,eager,eagle,ear,eardrum,earl,earmark,earn,earnest,earphone,earring,earth,earthy,ease,easel,east,eastern,eastward,easy,e
ave,ebb,ebony,echelon,echo,echoes,eclectic,eclipse,ecliptic,eclogue,ecology,economic,economy,ectoderm,ectopic,ecumenic,eddy,edge,edgewise,edging,edgy,
edict,edifice,edify,edit,edition,editor,educable,educate,eel,eerie,eerily,efface,effect,efferent,efficacy,effort,effusion,effusive,egg,eggplant,eggshe
ll,egress,egret,eider,eidetic,eight,eighteen,eighth,eighty,either,elapse,elastic,elate,elbow,elder,eldest,elect,elector,electret,electric,electro,elec
tron,elegant,elegiac,elegy,element,elephant,elevate,eleven,eleventh,elfin,elicit,elide,eligible,elision,elite,elk,ellipse,ellipsis,elliptic,elm,eloque
nt,else,eluate,elute,elution,elves,elysian,emaciate,emanate,embank,embargo,embark,embassy,ember,embezzle,emblazon,emblem,embody,embolden,emboss,embowe
r,embrace,emcee,emerald,emerge,emergent,emeriti,emeritus,eminent,emirate,emissary,emotion,empathy,emperor,emphases,emphasis,emphatic
,empire,empiric,emplace,employ,employed,employee,employer,emporium,empower,empress,empty,emulate,emulsify,emulsion,enable,enamel,enclave,encomia,encom
ium,encore,encroach,end,endemic,endgame,endoderm,endorse,endow,endpoint,endure,energy,enervate,enfant,engage,engine,engineer,enhance,enigma,e
nliven,enol,enough,enquire,enquiry,enrollee,ensconce,ensemble,entendre,enter,enthalpy,enthrall,entice,entire,entirety,entity,entrant,entropy,entry,env
elop,envelope,enviable,environ,envoy,enzyme,epic,epicure,epicycle,epidemic,epigram,epigraph,epilogue,episode,episodic,epistle,epitaph,epithet,epitome,
epoch,epochal,epoxy,epsilon,equable,equal,equate,equine,equinox,equip,equipped,equity,era,erasable,erase,erasure,erg,ergative,ergodic,erode,erodible,e
rosible,erosion,erosive,errand,errant,errantry,ersatz,erudite,erupt,eruption,escalate,escapade,escape,escapee,escheat,eschew,escort,escrow,esophagi,es
oteric,especial,espousal,espouse,esprit,esquire,essay,essence,estate,esteem,ester,estimate,estrange,estuary,etch,eternal,eternity,ethane,ethanol,ether
,ethereal,ethology,ethos,ethyl,ethylene,etude,eucre,eugenic,eulogy,eureka,eutectic,evacuate,evade,evaluate,evangel,eve,even,evensong,event,event
ful,eventide,eventual,every,everyday,everyman,everyone,evict,evident,evocable,evocate,evoke,evolve,evzone,ewe,exact,exacter,exalt,exam,examine,example
,excavate,exceed,excel,excelled,except,excerpt,excess,excise,excision,excite,exclaim,exclude,excuse,executor,exemplar,exempt,exercise,exert,exhale,exh
aust,exhort,exile,exist,existent,exit,exodus,expand,expanse,expect,expedite,expend,expense,expert,expiable,expiate,expire,explain,explicit,explode,exp
loit,explore,exponent,export,expose,exposit,exposure,expound,express,extant,extend,extensor,extent,exterior,external,extinct,extol,extolled,extoller,e
xtra,extract,extrema,extremal,extreme,extremis,extremum,extrude,exult,exultant,eye,eyeball,eyebrow,eyed,eyeful,eyeglass,eyelash,eyelet,eyelid,eyepiece
,eyesight';

  WORDS3 CONSTANT VARCHAR2(32726) := 'fable,fabric,fabulous,facade,face,facet,facial,facile,fact,factor,factory,factual,faculty,fad,fade,fadeout,fain,faint,fair,fairgoer,fairway,falcon,fal
conry,fall,fallen,fallible,falloff,fallout,fallow,fame,familial,familiar,familism,family,famine,famish,famous,fan,fanatic,fanciful,fancy,fanfare,fanfo
ld,fantasy,far,farce,fare,farewell,farm,farmland,fashion,fast,fasten,fate,fateful,father,fathom,fatigue,faucet,faun,fauna,fawn,feasible,feast,feat,fea
ther,feathery,feature,fed,federal,federate,fee,feed,feedback,feel,feet,feign,feint,feldspar,felicity,feline,fell,fellow,female,femur,fence,fend,fennel
,ferment,fern,fernery,ferocity,ferret,ferric,ferris,ferrite,ferrous,ferrule,ferry,fertile,fervent,fescue,fest,festival,festive,fetch,fete,fetid
,fetter,fettle,feud,feudal,fever,feverish,few,fiance,fiancee,fibrin,fibrosis,fibrous,fiche,fickle,fiction,fictive,fiddle,fide,fidelity,fidget,fiducial
,fief,fiefdom,field,fiend,fiendish,fierce,fiery,fiesta,fife,fifteen,fifth,fiftieth,fifty,fig,figure,figurine,filament,filbert,filch,file,filet,filial,
fill,filled,filler,fillet,filly,film,filmdom,filmy,filter,filtrate,fin,final,finale,finance,finch,find,fine,finery,finesse,finessed,finicky,fin
ish,finite,fir,fireboat,firebug,firefly,fireman,firemen,fireside,firewall,firewood,firework,firm,firmware,first,fiscal,fish,fishery,fishpond,fish
y,fission,fissure,fit,fitful,five,fivefold,fix,fixate,fixture,fizzle,fjord,flack,flag,flagpole,flagrant,flail,flair,flak,flake,flaky,flame,flamin
go,flange,flank,flannel,flap,flare,flash,flashy,flask,flat,flatbed,flathead,flatiron,flatland,flatten,flattery,flatware,flatworm,flaunt,flax,flaxen,fl
axseed,flea,fleck,fled,fledge,flee,fleece,fleeing,fleet,flemish,fletch,flew,flex,flexible,flick,flier,flight,flimsy,flinch,fling,flint,flinty,flippant
,flit,float,flood,floodlit,floor,flora,floral,florist,flotilla,flounder,flour,flourish,flow,flowery,flown,flu,flue,fluency,fluent,fluff,fluffy,f
luid,fluke,flung,fluoride,fluorine,fluorite,flurry,fluster,flute,flutter,fluvial,flux,fly,flyer,flyway,foal,foam,focal,foci,focus,focussed,foe,fog,fog
ging,foggy,fogy,foible,foil,foist,fold,foldout,foliage,foliate,folio,folk,folklore,folksong,folksy,follicle,follow,folly,fondly,font,food,foot,footage
,football,footfall,foothill,footman,footmen,footnote,footpad,footpath,footstep,footwear,footwork,for,forage,foray,forbade,forbear,forbid,forbore,forbo
rne,force,forceful,forcible,ford,fore,foreign,forest,forestry,forever,forfeit,forfend,forgave,forge,forget,forgive,forgiven,forgo,forgot,fork,forklift
,forlorn,form,formal,formant,format,formate,formic,formula,formulae,forsake,forsaken,forsook,forswear,fort,forte,forth,fortieth,fortify,fortin,fortior
i,fortran,fortress,fortune,forty,forum,forward,forwent,fossil,foster,fought,foul,found,fountain,four,fourfold,foursome,fourteen,fourth,fovea,fowl,fox,
foxglove,foxhole,foxhound,foxtail,foyer,fraction,fracture,fragile,fragment,fragrant,frail,frailty,frame,franc,franca,frank,franklin,fray,frayed,freckl
e,freed,freedmen,freedom,freehand,freehold,freeing,freeman,freemen,freer,freest,freeway,freeze,freight,frenetic,frenzy,freon,frequent,fresco,fres
coes,fresh,freshen,friction,fried,friend,frieze,frigid,frill,frilly,fringe,frisky,frizzle,fro,frock,frog,frolic,from,front,frontier,frost,frosty,froth
,frothy,frown,frowzy,froze,frozen,fructose,frugal,fruit,fruitful,fruition,fry,fudge,fuel,fugitive,fulcrum,fulfill,full,fullback,fully,fumble,fume,fumi
gant,fumigate,fun,function,fund,fungal,fungi,funnel,funny,fur,furbish,furious,furl,furlong,furlough,furnace,furnish,furrier,furrow,furry,further,furth
est,furtive,fury,furze,fuse,fuselage,fusible,fusion,fuss,fussy,futile,future,fuzz,fuzzy,gab,gabble,gable,gadget,gadgetry,gage,gaggle,gain,gainful,gait
,gal,gala,galactic,galaxy,gale,gall,gallant,gallery,galley,gallon,gallop,galvanic,gambit,gamble,game,gamesman,gamut,gander,gannet,gantlet,gantry,gap,g
garage,garb,garble,garden,gardenia,gargle,garish,garland,garlic,garner,garnet,garrison,garter,gash,gasket,gaslight,gasoline,gasp,gate,gatekeep,gat
eway,gather,gator,gauche,gaudy,gauge,gaunt,gauntlet,gauss,gauze,gave,gavel,gavotte,gawk,gaze,gazelle,gazette,gear,gecko,geese,gel,gelable,gelatin,gela
tine,gem,geminate,gemlike,gemstone,gene,genera,general,generate,generic,generous,genesis,genetic,genial,genie,genius,genotype,genre,gent,genteel,genti
an,gentile,gentle,gentry,genuine,genus,geometer,geranium,germ,germane,germinal,gerund,gestalt,gesture,get,getaway,geyser,ghastly,ghost,ghostly,ghoul,g
houlish,giant,giantess,gibbet,gibbon,giblet,giddy,gift,gig,gigaherz,gigantic,gigavolt,gigawatt,giggle,gila,gild,gill,gilt,gimmick,ginger,gingham,gingk
o,ginseng,giraffe,girl,girth,gist,give,giveaway,given,giveth,glacial,glaciate,glacier,glad,gladden,glade,glamor,glamour,glance,gland,glare,glass,glass
y,glaucoma,glaucous,glaze,gleam,glean,glee,gleeful,glen,glib,glide,glimmer,glimpse,glint,glissade,glisten,glitch,glitter,gloat,glob,global,globe,globu
lar,globule,globulin,gloom,gloomy,glorify,glorious,glory,gloss,glossary,glossed,glossy,glottal,glottis,glove,glow,glucose,glue,glued,gluey,gluing,glum
,glut,glutamic,glutton,glycerin,glycerol,glycine,glycogen,glycol,glyph,gnarl,gnash,gnat,gnaw,gnome,gnu,go,goad,goal,goat,gobble,goblet,goes,gold,golde
n,goldfish,golf,gondola,gone,good,goodbye,goodwill,goose,gopher,gore,gorge,gorgeous,gorgon,gorilla,gorse,gory,gosling,gossamer,gossip,got,gouge,gourd,
gourmet,gout,govern,governor,gown,grab,grace,graceful,gracious,grackle,grad,gradate,grade,gradient,gradual,graduate,graft,graham,grail,grain,grainy,gr
ammar,granary,grand,grandeur,grandma,grandpa,grandson,granite,granitic,granny,granola,grant,grantee,grantor,granular,granule,grape,graph,graphic,graph
ite,grapple,grasp,grass,grassy,grate,grateful,grater,gratify,gratuity,grave,gravel,graven,gravid,gravy,gray,grayish,graze,grease,greasy,great,greater,
grebe,greed,greedy,green,greenery,greenish,greet,grew,grey,grid,griddle,gridiron,grief,grieve,grievous,griffin,grill,grille,grilled,grim,grimace,grime
,grin,grind,grip,gripe,grisly,grist,grit,gritty,grizzle,grizzly,groan,groat,grocer,grocery,groggy,groom,groove,gross,ground,group,grout,grove,grovel,g
row,growl,grown,grownup,growth,grudge,gruff,grumble,grunt,gryphon,guanine,guard,guardian,guerdon,guernsey,guess,guest,guffaw,guidance,guide,g
uiding,guignol,guild,guile,guinea,guise,guitar,gulf,gull,gullet,gullible,gully,gulp,gum,gumbo,gumdrop,gummy,gumption,gumshoe,gurgle,guru,gush,gusset,g
ust,gusto,gusty,gut,gutsy,guttural,guy,guzzle,gym,gymnast,gypsite,gypsum,gypsy,habit,habitant,habitat,habitual,hacksaw,had,haddock,hadron,haggard,hagg
le,haiku,hail,hair,haircut,hairdo,hairpin,hale,half,halfback,halfway,halibut,halide,halite,hall,hallmark,hallow,hallway,halo,halogen,halt,halve,ham,ha
mlet,hammock,hamper,hamster,hand,handbag,handbook,handful,handle,handmade,handout,handset,handsome,handy,handyman,handymen,hang,hangar,hansom
,happen,happy,harass,hardbake,harden,hardhat,hardtack,hardtop,hardware,hardwood,hardy,hare,hark,harm,harmful,harmonic,harmony,harness,harp,harpoon,har
row,harsh,harshen,hart,harvest,hassle,hast,haste,hasten,hasty,hat,hatch,hatchet,hath,haul,haulage,haunch,haunt,have,haven,havoc,hawk,hay,hayfield,hays
tack,hayward,hazard,haze,hazel,hazelnut,hazy,he,head,headache,headland,headline,headroom,headset,headsman,headsmen,headwall,headway,headwind,heal,heal
th,healthy,heap,hear,heard,hearken,heart,hearten,hearth,hearty,heat,heater,heath,heathen,heave,heaven,heavy,hectic,hector,hedge,hedgehog,heed,heel,hef
t,hefty,hegemony,height,heighten,heir,heiress,held,helical,helium,helix,hello,helm,helmet,helmsman,helmsmen,help,helpful,helpmate,hem,hematite,hemlock
,hen,hence,henpeck,heptane,her,herald,herb,herd,herdsman,here,hereby,heredity,heretic,hereto,hereunto,herewith,heritage,hero,heroes,heroic,heroism,her
on,herself,hertz,hesitant,hesitate,hewn,hex,hexagon,hexane,hey,heyday,hi,hiatus,hibachi,hickory,hid,hidden,hide,hideaway,hideous,hideout,hieratic,high
est,highland,highroad,hightail,highway,hike,hilarity,hill,hillside,hilltop,hilly,him,himself,hindmost,hinge,hint,hip,hippo,hire,hireling,his,hiss,hist
oric,history,hit,hitch,hither,hitherto,hive,hoard,hoarse,hobble,hobby,hockey,hodge,hold,holden,holiday,holler,hollow,holly,hologram,holster,holt,homag
e,home,homeland,homemade,homesick,homeward,homework,homology,homonym,hone,honest,honesty,honey,honeybee,honeydew,honorary,honoree,hoof,hoofmark,hook,h
ookworm,hoop,hoot,hooves,hop,hope,hopeful,horde,horizon,horn,hornet,horse,horsedom,horsefly,horseman,horsemen,hose,hosiery,hospice,hospital,host,hosta
ge,hostess,hotel,hotelman,hound,hour,house,housefly,hove,hovel,hover,how,howdy,however,howl,hub,hubris,huddle,hue,hued,huff,hug,huge,hugging,h
ull,hum,human,humane,humanoid,humble,humerus,humid,humidify,humility,hummock,humorous,humus,hunch,hundred,hungry,hunt,hurdle,hurl,hurley,hurrah,hurray
,hurry,hurt,hurtle,husband,hush,hut,hutch,hyacinth,hyaline,hybrid,hydra,hydrant,hydrate,hydride,hydro,hydrogen,hydrous,hydroxy,hydroxyl,h
yena,hygiene,hymn,hymnal,hyphen,hypnosis,hypnotic,iambic,ice,iceberg,icebox,iceland,icicle,icon,iconic,icy,idea,ideal,identify,identity,ideology,idiom
,idle,idyll,idyllic,if,igloo,igneous,ignite,ignition,ileum,iliac,illogic,illume,illumine,illusion,illusive,illusory,image,imagery,imagine,imbibe,imbru
e,imbue,imitable,imitate,immanent,immature,immense,immerse,imminent,immobile,immune,impact,impair,impale,impart,impasse,impeach,impede,impel,impelled,
impeller,impend,imperate,imperial,imperil,impetus,impiety,impinge,implicit,implore,import,impose,impost,impound,impress,imprint,improve,impudent,impug
n,impulse,impunity,impute,inaction,inactive,incant,incense,inceptor,inch,incident,incise,incisive,incite,incline,inclose,include,income,increase,incub
ate,incur,incurred,incurrer,indebted,indeed,indent,index,indicant,indicate,indices,indict,indigene,indigent,indigo,indirect,indoor,indorse,induce,indu
ct,inductee,inductor,industry,inequity,inert,inertia,inertial,inexact,inexpert,infancy,infant,infantry,infer,inferred,infield,infight,infinite,infinit
y,infirm,inflate,inflater,inflect,inflict,inflow,influent,influx,inform,informal,infract,infrared,infringe,infuse,infusion,ingather,ingest,ingrown,inh
abit,inhale,inhere,inherent,inherit,inhibit,inhuman,inimical,iniquity,initial,initiate,inject,injunct,injure,injury,ink,inkling,inlaid,inland,inlay,in
let,inn,innate,inner,innocent,innovate,input,inquest,inquire,inquiry,inroad,inscribe,insect,insecure,insert,inset,inshore,inside,insig
ht,insignia,insist,insomnia,inspect,inspire,install,instance,instant,instead,instep,instill,instinct,instruct,insular,insulate,insulin,insult,insure,i
ntact,intake,integer,integral,intend,intense,intent,inter,intercom,interest,interim,interior,intermit,intern,internal,interval,into,intonate,intone,in
trepid,intrigue,introit,intrude,intuit,inundate,inure,invade,invasion,invasive,inveigh,inveigle,invent,inventor,inverse,invert,invest,investor,inviabl
e,invite,invitee,invocate,invoice,invoke,involve,inward,iodide,iodine,ion,ionic,irate,iridium,iris,iron,ironic,ironside,ironwood,is,island,isle,isolat
e,isomer,isomorph,isotherm,isotope,isotopic,isotropy,issuance,issuant,issue,it,italic,item,iterate,itself,ivory,ivy,jacket,jackpot,jade,jag,jaguar,jam
boree,jangle,janitor,jar,jargon,jasper,jaundice,jaunty,javelin,jaw,jawbone,jawbreak,jazz,jazzy,jelly,jersey,jest,jet,jetliner,jettison,jewel,jewelry,j
ig,jigsaw,jilt,jingle,jittery,jive,jockey,jog,jogging,joggle,join,jolly,jolt,jostle,jot,joule,jounce,journal,journey,joust,jovial,jowl,jowly,joy,j
oyful,joyous,jubilant,jubilate,jubilee,judge,judicial,judo,jug,jugate,jugging,juggle,juice,jumble,jump,junction,junctor,juncture,jungle,junior,j
uniper,juror,jury,just,justice,justify,jut,juvenile,kaiser,kale,kangaroo,keel,keen,keep,kept,kerchief,kern,kernel,kerosene,ketch,ketchup,ketone,ketosi
s,kettle,key,keyboard,keyed,keyhole,keynote,keys,keystone,khaki,kickoff,kid,kidney,kilo,kilohm,kimono,kin,kind,kindle,kindred,kinesic,kinetic,king,kin
gbird,kingdom,kiosk,kit,kitchen,kite,kitten,kittle,kitty,kiwi,knack,knapsack,knead,knee,kneecap,kneel,knell,knelt,knew,knick,knight,knit,knobby,knock,
knockout,knoll,knot,knotty,know,knoweth,knowhow,known,knuckle,knurl,koala,kosher,kraft,lab,label,labour,lace,lacerate,laconic,lacquer,lacrosse,lacuna,
lacunae,lacy,lad,laden,ladle,lady,ladyfern,lag,lagging,lagoon,lain,lair,laissez,laity,lake,lakeside,lam,lamb,lament,laminate,lamp,lampoon,lamprey,lanc
e,land,landfill,landhold,landlord,landmark,lane,language,languid,languish,lanky,lantern,lap,lapel,lapelled,lapidary,lapse,larch,large,largesse,lariat,
lark,larkspur,larva,larvae,larval,larynges,larynx,lash,lasso,last,latch,late,latent,later,latera,lateral,laterite,lath,lathe,latitude,latter,lattice,l
atus,laud,laudanum,laugh,laughter,launch,laundry,laureate,laurel,lava,lavatory,lavender,lavish,law,lawful,lawn,lawyer,lay,layette,layman,laymen,layoff
,layout,layup,laze,lazy,lea,lead,leaden,leadeth,leadsman,leadsmen,leaf,leaflet,leafy,league,leak,leakage,leaky,lean,leap,leapfrog,leapt,learn,lease,le
ash,least,leather,leathery,leave,leaven,lectern,lecture,led,ledge,leek,leer,leery,leeward,leeway,left,leftmost,leftover,leftward,lefty,leg,legacy,lega
l,legate,legato,legend,legging,legible,legion,leisure,lemming,lemon,lemonade,lend,length,lengthen,lengthy,lenient,lens,lentil,leonine,leopard,lesson,l
essor,lest,let,lethal,lethargy,lettuce,leucine,levee,level,lever,leverage,levitate,levity,levulose,levy,lew,lexical,lexicon,liable,liaison,liberal,lib
erate,liberty,library,librate,libretto,licensee,licensor,lichen,licorice,lid,lien,lieu,life,lifeboat,lifelike,lifelong,lifespan,lifetime,lift,ligament
,ligand,ligature,light,lighten,like,liken,likewise,lilac,lilt,lily,lim,limb,limbic,lime,linden,line,lineage,lineal,linear,lineman,linemen,linen,l
ineup,linger,lingual,linguist,liniment,link,linkage,linoleum,linseed,lint,lion,lioness,lip,lipid,lipread,lipstick,liquefy,liquid,liquidus,lisle,list,l
isten,lit,litany,literacy,literal,literary,literate,lithe,lithic,lithium,litigant,litigate,litmus,little,live,liven,livery,liveth,livid,livre
,lizard,load,loaf,loam,loamy,loan,loaves,lobby,lobe,lobster,lobular,lobule,local,locale,locate,loci,lock,lockout,lockstep,lockup,locomote,locus,locust
,locution,locutor,lodge,loft,lofty,log,loge,logging,logic,logician,logistic,logjam,logo,lollipop,lone,lonesome,long,longhand,longhorn,longish,longleg,
longtime,look,lookout,lookup,loom,loop,loophole,loose,loosen,loot,lop,lope,lopseed,lopsided,lore,losable,loss,lossy,lost,lot,lotion,lottery,lotus,loud
,lounge,love,lovebird,lovelorn,low,lowdown,lower,lowland,loy,loyal,loyalty,lozenge,lucid,luck,lucky,lug,luge,luggage,lugging,lukewarm,lull,lullaby,lum
bar,lumber,lumen,luminary,luminous,lump,lunar,lunary,lunate,lunch,luncheon,lung,lunge,lupine,lurch,lure,lurk,lute,lutetium,lux,luxe,luxury,lyric';

  WORDS4 CONSTANT VARCHAR2(32726) := 'machination,machine,mackerel,macro,madam,made,madrigal,maestro,magazine,magenta,magi,magic,magician,magma,magnate,magnesia,magnet,magnetic,magnify,mag
nolia,magnum,mahogany,maid,maiden,mail,mailbox,mailman,mailmen,maim,main,mainland,mainline,mainstay,maintain,majestic,majesty,major,make,makeup,malada
pt,malady,malaprop,malaria,malarial,male,maledict,mall,mallard,mallet,mallow,malposed,malt,maltose,maltreat,mambo,mammal,mammoth,man,manage,manatee,ma
ndarin,mandate,mandrake,mandrel,mandrill,mane,maneuver,mange,mangle,manhole,manhood,manic,manifest,manifold,mankind,manna,manor,mansion,mantel,mantiss
a,mantle,manual,manumit,many,map,maple,mar,marathon,maraud,marble,march,mare,margin,marginal,marigold,marimba,marina,marinade,marinate,marine,marital,
maritime,mark,market,marlin,marmot,maroon,marquee,marquess,marquis,marriage,married,marrow,marry,marsh,marshal,mart,martial,marvel,mascara,mash,mask,m
ason,masonry,mass,massage,masseur,massive,mast,mastery,mastodon,mat,match,mate,material,materiel,maternal,matinee,matrices,matrix,matron,matte,mattres
s,maturate,mature,maudlin,maul,mauve,maverick,max,maxima,maximal,maximum,may,maybe,mayor,mayoral,maze,me,mead,meadow,meager,meal,mealtime,meander,mean
t,meantime,measle,measure,meat,meaty,mechanic,medal,meddle,media,medial,median,mediate,medic,medicate,medicine,meditate,medium,medlar,medley,meek,meet
,megabit,megabyte,megaton,megavolt,megawatt,megaword,megohm,meiosis,melamine,melange,melanin,melanoma,meld,melee,mellow,melodic,melody,melon,melt,melt
down,member,membrane,memento,memo,memoir,memorial,memory,men,menace,mend,menial,meniscus,mention,mentor,menu,merchant,mercuric,mercury,mere,merge,meri
dian,meringue,merit,merlin,mermaid,merry,mesa,mesh,mesmeric,mesoderm,meson,mesquite,mess,message,messy,met,metabole,metal,metallic,metaphor,meteor,met
eoric,meter,methane,methanol,method,methodic,metric,metro,mettle,mew,mezzo,mica,mice,micro,micron,mid,midband,midday,middle,midnight,midpoint,midrange
,midscale,midspan,midst,midterm,midway,midweek,might,mighty,mignon,migrant,migrate,mild,mildew,mile,mileage,milieu,militant,military,militate,milk,mil
kweed,milky,mill,millenia,miller,millet,mill,millions,mimesis,mimetic,mimic,mimicked,min,minaret,mince,mind,mindful,mine,mineral,mingle,mini,minibike,minimal,minimum,minion,ministry,mink,minnow,minor,minstrel,mint,minuet,minus,minute,minutiae,miracle,mirage,mire,mirror,mirth,miscible,miser,misnomer,
miss,missile,mission,missive,mist,misty,mite,mitigate,mitosis,mitral,mitre,mitt,mitten,mix,mixture,mixup,mnemonic,moat,mobile,mobility,moccasin,mock,m
ockery,mockup,modal,mode,model,modem,moderate,modern,modest,modesty,modify,modish,modular,modulate,module,moduli,modulo,modulus,modus,moiety,moire,moi
st,moisten,moisture,molal,molar,molasses,mold,mole,molecule,molehill,mollify,mollusk,molt,molten,moment,momenta,momentum,mommy,monad,monadic,monarch,m
onarchy,monastic,monaural,monel,monetary,money,mongoose,monic,monies,monitor,monitory,monkey,monoid,monolith,monomer,monomial,monopoly,monoxide,monsoo
n,monster,montage,monte,month,monument,mood,moody,moon,moonlit,moor,moose,moot,mop,moral,morale,more,morel,moreover,morn,morose,morpheme,morphism,morr
ow,morsel,mort,mortal,mortar,mortem,mortgage,mortify,mortise,mosaic,mosque,mosquito,moss,mossy,most,mot,motel,moth,mothball,mother,motif,motion,motiva
te,motive,motley,motor,mottle,motto,mound,mount,mountain,mourn,mournful,mouse,mousy,mouth,mouthful,move,movie,mow,much,mucosa,mucus,mud,muddle,muddy,m
udguard,mudsling,muffin,muffle,mug,muggy,mulberry,mulch,mule,mulish,mull,mulligan,multi,multiple,multiply,mum,mumble,mummy,munch,mundane,mung,munition
,muon,mural,murk,murky,murmur,muscle,muscular,museum,mush,mushroom,mushy,music,musicale,musician,musk,muskox,muskoxen,muskrat,mussel,must,mustache,mus
tang,mustard,musty,mutagen,mutandis,mutant,mutate,mutatis,mute,mutineer,mutiny,mutt,mutter,mutton,mutual,mutuel,muzzle,my,mycology,myel,myeline,myeloi
d,mylar,mynah,myopia,myopic,myosin,myriad,myrtle,myself,mystery,mystic,mystify,mystique,myth,mythic,nab,nag,nagging,naiad,nail,name,nameable,namesake,
nap,nape,napkin,narrate,narrow,nary,nasal,nascent,natal,nation,native,natural,nature,nautical,nautilus,naval,nave,navigate,navy,neap,near,nearby
,nearest,neat,neater,neath,nebula,nebulae,nebular,nebulous,neck,necklace,neckline,necktie,nectar,nectary,need,needful,needham,needle,neither,nemesis,n
eon,neonatal,neonate,neophyte,neoprene,nepenthe,nephew,nerve,nest,nestle,net,nether,nettle,network,neural,neuritis,neuron,neuronal,neutral,neutrino,ne
utron,never,new,newborn,newcomer,newel,newfound,newline,newlywed,newsboy,newscast,newsman,newsmen,newsreel,newt,newton,next,niacin,nibble,nice,nicety,
niche,nick,nickel,nickname,niece,nigh,night,nimble,nimbus,nine,ninebark,ninefold,nineteen,ninety,ninth,nitrate,nitric,nitride,nitrite,nitrogen,no,
nobelium,noble,nobleman,noblemen,noblesse,nobody,nocturne,nod,nodal,node,nodular,nodule,noise,noisy,nomad,nomadic,nominal,nominate,nominee,none,noodle
,nook,noon,noontime,nor,north,northern,nose,nostril,not,notary,notch,note,notebook,notice,notify,notion,noun,nourish,nouveau,nova,novel,novelty,novice
,now,nowaday,nowadays,nowhere,nozzle,nuance,nucleate,nuclei,nucleic,nucleoli,nucleus,nuclide,nudge,nugget,numb,numeral,numerate,numeric,numerous,numin
ous,nurse,nursery,nurture,nutmeg,nutrient,nutshell,nuzzle,nylon,oak,oaken,oakwood,oar,oasis,oat,oath,oatmeal,obduracy,obdurate,obelisk,obey,object,obj
ector,objet,oblate,obligate,oblige,oblique,oblong,oboe,oboist,observe,obsidian,obsolete,obstacle,obstruct,obtain,obtrude,obverse,obviate,obvious,occas
ion,occident,occlude,occupant,occupy,occur,occurred,ocean,oceanic,ocelot,octagon,octal,octane,octant,octave,octet,octile,octopus,ocular,ode,odious,odi
um,odometer,oersted,of,off,offal,offbeat,offend,offer,offhand,office,official,officio,offload,offset,offshoot,offshore,offstage,oft,often,ogle,ohm,ohm
ic,ohmmeter,oil,oilcloth,oilman,oilmen,oilseed,oily,oint,ointment,okay,old,olden,oldster,oldy,oleander,olefin,olive,olivine,omelet,omicron,ominous,omn
ibus,on,once,oncology,oncoming,one,onerous,oneself,onetime,ongoing,onion,only,onrush,onset,onto,ontogeny,ontology,onus,onward,onyx,oocyte,opacity
,opal,opaque,open,opera,operable,operand,operant,operate,operatic,operetta,operon,opine,opinion,opossum,opponent,oppose,opposite,opt,optic,optima,opti
mal,optimism,optimist,optimum,option,opulent,or,oracle,oracular,orange,orate,oratoric,oratorio,oratory,orb,orbit,orbital,orchard,orchid,ordain,ordeal,
order,orderly,ordinal,ordinary,ordinate,ordnance,ore,oregano,organic,origin,original,oriole,ornament,ornate,ornately,orthant,orthicon,orthodox,osier,o
smium,osmosis,osmotic,osprey,osseous,ossify,ostrich,other,otiose,otter,ought,ounce,our,oust,out,outlawry,oval,oven,over,overhang,overt,overture,owl,ow
ly,own,ox,oxalate,oxalic,oxcart,oxen,oxeye,oxidant,oxidate,oxide,oxygen,oyster,ozone,pace,pacemake,pacific,pacifism,pacifist,pacify,pack,package,packe
t,pact,pad,paddock,paddy,padlock,page,pageant,paginate,paid,pail,paint,pair,pairwise,pal,palace,palate,palazzi,palazzo,pale,palette,palfrey,palisade,p
all,palladia,pallet,palliate,pallid,palm,palmate,palmetto,palpable,palsy,pamper,pamphlet,pan,panacea,panama,pancake,pancreas,panda,pane,panel,panorama
,pant,pantheon,panther,pantry,papa,papery,paprika,papyrus,par,parabola,parade,paradigm,paradise,paradox,paraffin,paragon,parakeet,parallax,parallel,pa
rasite,parasol,paraxial,parboil,parcel,parch,pardon,pare,parent,parental,pariah,park,parkish,parkland,parkway,parody,parquet,parrot,parry,parse,parsle
y,parsnip,parson,part,partake,partial,particle,partisan,partner,partook,party,paschal,pass,passage,passband,passe,passer,passerby,passion,passive,pass
port,past,paste,pastel,pastime,pastoral,pastry,pasture,pat,patch,patchy,pate,patent,patentee,pater,paternal,path,pathfind,pathogen,pathway,patient,pat
io,patriot,patrol,patron,pattern,pause,pave,paw,pawn,pawnshop,pax,pay,paycheck,payday,payoff,payroll,pea,peace,peaceful,peach,peacock,peak,peal,peanut
,pear,pearl,peat,pebble,pecan,peccary,pectoral,peculate,peculiar,pedagogy,pedal,pedant,pedantic,pedantry,peddle,pedestal,pedigree,pediment,peek,p
eel,peg,pegboard,pegging,pelican,pellet,pelt,pemmican,pen,penalty,penance,pence,penchant,pencil,pend,pendant,pendulum,penguin,penitent,penman,penmen,p
ennant,penny,pension,pensive,pent,pentagon,pentane,penumbra,penury,people,pep,peppery,peptide,per,perceive,percent,percept,perch,perfect,perfidy,perfo
rce,perform,perfume,perfuse,perhaps,peril,perilous,period,periodic,perjure,perjury,perk,permeate,permit,permute,peroxide,perplex,persist,person,person
a,personal,persuade,pert,pertain,perturb,perusal,peruse,pervade,pestle,pet,petal,petit,petite,petition,petrel,petri,petrify,petrol,petty,petulant,petu
nia,pewter,pharmacy,phase,pheasant,phenol,phenolic,phenyl,phlox,phobic,phoebe,phoenix,phone,phoneme,phonemic,phonetic,phonic,phosgene,phosphor,photo,p
hoton,phrase,physique,pi,pianist,piano,piccolo,pickerel,picket,pickle,picky,picnic,picture,piddle,pidgin,pie,piece,pier,pierce,pietism,piety,pigeo
n,pigment,pigpen,pigskin,pigtail,pike,pile,pilfer,pilgrim,pill,pillage,pillar,pillory,pillow,pilot,pin,pinafore,pinball,pinch,pine,ping,pinhole,pinion
,pink,pinkie,pinkish,pinnacle,pinnate,pinochle,pinpoint,pinscher,pint,pintail,pinwheel,pioneer,pipeline,pipette,piquant,pique,piracy,pirate,pisto
n,pit,pitch,piteous,pitfall,pith,pithy,pivot,pivotal,pixel,pixy,pizza,pizzeria,placate,placater,place,placebo,placid,plaid,plain,plan,planar,plane,pla
net,plank,plankton,plant,plantain,plaque,plasm,plasma,plaster,plastic,plate,plateau,platelet,platform,platinum,platonic,platoon,platypus,play,playback
,playful,playoff,playroom,playtime,plaza,plea,plead,pleasant,please,pleat,plebeian,plebian,pledge,plenary,plenty,plenum,plethora,pleura,pleural,pliabl
e,pliancy,pliant,pliers,plight,plot,plover,plowman,pluck,plug,plugging,plum,plumage,plumb,plume,plummet,plunge,plunk,plural,plus,plush,pluton,ply,plyw
ood,poach,pocket,pod,podium,poem,poesy,poet,poetic,poetry,pogo,poignant,point,poise,polar,polaron,pole,polecat,polemic,police,policy,polio,polite
,politic,politico,polity,polka,polkadot,poll,pollen,pollock,pollster,pollute,polo,polonium,polopony,polyglot,polygon,polymer,polytope,polytypy,pomade,
pomp,pompano,pompey,pompous,poncho,pond,ponder,pony,poodle,pool,pop,popcorn,poplar,poplin,poppy,populace,popular,populate,populism,populist,populous,p
orch,pore,porosity,porous,porpoise,porridge,port,portage,portal,portend,portent,portico,portland,portrait,portray,posable,pose,posey,posh,posi
tion,positive,positron,posse,posseman,possemen,possess,possible,possum,post,postage,postcard,postfix,postman,postmark,postmen,postpaid,postpone,postur
e,posy,potable,potato,potatoes,potent,pothole,potion,potlatch,pottery,pouch,poultry,pounce,pound,pour,pout,powder,powdery,power,powerful,prac
tice,prairie,praise,prance,preamble,precept,precess,precinct,precious,precise,preclude,preempt,preen,preface,prefect,prefer,prefix,prelude,pr
emier,premiere,premise,premium,prep,prepare,presage,presence,present,preserve,preside,press,pressure,prestige,presume,presumed,pretend,pretense,pretex
t,pretty,prevail,prevent,preview,previous,prey,price,prickle,pride,prim,prima,primacy,primal,primary,primate,prime,primeval,primp,primrose,prince,prin
cess,print,printout,prior,priori,priory,prism,pristine,privacy,private,privet,prize,pro,probate,probe,proceed,process,proclaim,procure,prod,prodigal,p
rodigy,produce,product,profess,profile,profit,profound,profuse,progeny,progress,prohibit,project,prolate,proline,prologue,prolong,prom,promise,promote
,prompt,prone,pronoun,proof,prop,propane,propel,proper,property,proposal,propose,propound,propyl,prorate,prorogue,prosaic,prose,prosodic,prosody,prosp
ect,prosper,protean,protease,protect,protege,protein,protest,protocol,proton,protract,proud,prove,proven,proverb,provide,province,proviso,provoke,prov
ost,prow,prowess,prowl,proximal,prudent,prune,prurient,pry,pseudo,psyllium,puddly,puerile,puffery,puffin,puissant,pull,pulley,pullover,pulp,pulpit,pul
sar,pulsate,pulse,puma,pumice,pump,pumpkin,pun,punctual,puncture,pundit,punditry,pungent,punster,punt,pup,pupate,pupil,puppet,puppy,puppyish,purchase,
pure,purge,purify,purl,purple,purport,purpose,purse,purslane,pursuant,pursue,pursuer,pursuit,purvey,purveyor,purview,push,pushout,pushpin,put,putative
,putt,putty,puzzle,pygmy,pyknotic,pyramid,python,quad,quadrant,quadric,quaff,quagmire,quail,quaint,quake,qualify,quality,qualm,quandary,quanta,qu,quan
tile,quantity,quantum,quark,quarrel,quarry,quart,quartet,quartic,quartile,quartz,quasar,quash,quasi,quatrain,quaver,quay,quell,quench,quest,question,q
uetzal,queue,quibble,quick,quicken,quiet,quietus,quill,quilt,quince,quinine,quint,quintet,quintic,quintus,quip,quipping,quirk,quirt,quite,quitting,qui
ver,quixotic,quiz,quizzes,quonset,quorum,quota,quote,quotient,rabbit,rabble,raccoon,race,raceway,racket,rackety,radar,radial,radian,radiant,radiate,ra
dical,radices,radii,radio,radish,radium,radius,radon,raffle,raft,ragout,raid,rail,railbird,railhead,raillery,railroad,railway,rain,rainbow,raincoat,ra
indrop,rainfall,rainy,raise,raisin,raj,rajah,rake,rakish,rally,ram,ramble,ramify,ramp,rampage,rampant,rampart,ran,ranch,rancho,random,rang,range,rank,
rankle,ransack,ransom,rapid,rapier,rapport,rapture,rare,rarefy,rascal,rash,rasp,rate,rater,rather,ratify,ratio,rattail,rattle,ratty,raucous,ravage,rav
e,ravel,raven,ravenous,ravine,raw,rawhide,ray,raze,razor,razzle,reach,reactant,read,readout,ready,real,realm,realtor,realty,ream,reap,reason,reave,reb
,receipt,receive,recent,receptor,recess,recipe,recital,reckon,recline,recluse,recovery,recruit,rectify,rector,rectory,recur,recurred,recusant,recuse,r
ed,redact,redactor,redbird,redcoat,redden,reddish,redhead,reduce,redwood,reed,reef,reek,reel,refer,referee,referent,referral,referred,refinery,reflect
,refract,refrain,refuge,refugee,refusal,refute,regal,regale,regalia,regard,regatta,regent,regime,regimen,regiment,region,regional,registry,regular,reg
ulate,rehearse,reign,rein,reindeer,rejoice,relate,relayed,relevant,reliant,relic,relict,relief,relieve,religion,relish,remark,remedy,remember,remiss,r
emit,remitted,remnant,remorse,remote,removal,rend,render,renewal,renounce,renovate,renown,rent,rental,repartee,repeal,repeat,repeater,repel,repelled,r
epent,replete,replica,report,reprieve,reprisal,reprise,reproach,reptile,republic,repute,request,require,requited,rerouted,rescind,rescue,resemble,rese
nt,reserve,reside,resident,residual,residue,residuum,resign,resin,resist,resistor,resolute,resolve,resonant,resonate,resort,respect,respire,respite,re
spond,response,rest,restful,restive,restrain,restrict,result,resume,resuming,retail,retain,reticent,retina,retinal,retinue,retire,retiree,retort,retra
ct,retrieve,retrofit,return,reveal,revel,revelry,revenue,rever,revere,reverend,reverent,reverie,reversal,reverse,revert,revery,revile,revisal,revise,r
evision,revival,revive,revoke,revolt,revolve,revved,revving,reward,rhapsody,rhenium,rheology,rheostat,rhesus,rhetoric,rheum,rhino,rhodium,rhombic,rhom
bus,rhubarb,rhyme,rhythm,rhythmic,rib,ribald,ribbon,ribose,ribosome,rice,rich,ricochet,rid,riddance,ridden,riddle,ride,ridge,riffle,rift,rig,rigging,r
ight,rightful,rigid,rigorous,ring,ringlet,ringside,rink,rinse,rip,ripen,ripple,rise,risen,risible,rite,ritual,rival,rivalry,riven,river,riverine,r
ivet,rivulet,road,roadbed,roadside,roadster,roadway,roam,roar,roast,robe,robin,robot,robotic,robotics,robust,rock,rockabye,rockaway,rocket,rocky,rode,
rodent,rodeo,roe,roebuck,rogue,roil,roister,role,roll,rollback,rollick,romance,romp,roof,rooftop,rook,rookie,room,roomful,roommate,roomy,roost,root,ro
pe,rose,rosebud,rosebush,rosemary,rosette,roster,rosy,rotary,rotate,rote,rotor,rotunda,rouge,roughen,round,roundoff,roundup,rout,route,routine,rove,ro
w,rowboat,royal,royalty,rubble,rubdown,rubicund,rubidium,rubric,ruby,ruckus,rudder,ruddy,rudiment,ruff,ruffle,rufous,rug,rule,rumble,rumen,ruminant,ru
minate,rummage,run,runabout,runaway,rundown,rune,rung,runic,runneth,runoff,runway,rupee,rupture,rural,ruse,rush,rusk,russet,russula,rust,rustic,rustle
,rusty,rut,rutabaga,ruthless,rutile,rutty,rye';

  WORDS5 CONSTANT VARCHAR2(32726) := 'sabbatical,sable,sabotage,saccade,sachem,sack,saddle,safari,safe,safety,saffron,sag,saga,sagacity,sage,sagging,sagittal,said,sail,sailboat,sailfish,sa
ilor,saint,sake,salad,salami,salaried,salary,sale,salesman,salesmen,salient,saline,sallow,sally,salmon,salon,saloon,salt,salty,salutary,salute,salvage
,samba,same,sample,sanctify,sanction,sanctity,sand,sandal,sandbag,sandhill,sandman,sandpile,sandwich,sandy,sane,sang,sangaree,sanguine,sanicle,sanitar
y,sanitate,sank,sans,sapiens,sapient,sapling,saponify,sapphire,sarcasm,sarcoma,sardine,sardonic,sari,sash,sashay,sat,satiable,satiate,satiety,satin,sa
tire,satiric,satisfy,saturate,sauce,saucepan,saucy,saute,sauterne,savage,savagery,savant,save,savoy,savvy,saw,sawbelly,sawdust,sawfish,sawfly,sawmill,
sawtooth,sawyer,say,scabbard,scabrous,scaffold,scalar,scald,scale,scallop,scalp,scan,scandal,scandium,scant,scanty,scapula,scapular,scar,scarce,scare,
scarf,scarify,scarlet,scarves,scary,scathe,scavenge,scenario,scene,scenery,scenic,scent,schedule,schema,scheme,scholar,school,schooner,sciatica,scion,
scissor,scoff,scold,scoop,scoot,scope,scorch,score,scoria,scorn,scornful,scorpion,scotch,scour,scout,scowl,scrabble,scram,scramble,scrap,scrape,scratc
h,scratchy,scrawl,scream,screech,screechy,screen,scribble,scribe,script,scriven,scroll,scrooge,scrub,scruple,scrutiny,scuba,scuff,scuffle,scull,sculpi
n,sculpt,sculptor,scurry,scurvy,scuttle,scutum,scythe,sea,seaboard,seacoast,seafare,seafood,seagull,seahorse,seal,sealant,seam,seaport,seaquake,sear,s
earch,seashore,seaside,season,seasonal,seat,seater,seaward,seaweed,secant,secede,seclude,second,secrecy,secret,secrete,section,sector,secular,secure,s
edan,sedate,sedge,sediment,see,seeable,seed,seedbed,seedling,seeing,seek,seem,seen,seep,seepage,seethe,seethed,seething,segment,seismic,seize,
seldom,select,selector,selenate,selenite,selenium,self,sell,seller,sellout,seltzer,selves,semantic,semester,semi,seminar,seminary,semper,senate,send,s
enile,senior,sense,sensible,sensor,sensory,sent,sentence,sentient,sentinel,sentry,sepal,separate,septate,septum,sequel,sequent,sequin,sequitur,seraphi
m,serenade,serene,serge,sergeant,serial,seriate,seriatim,series,serif,serine,serious,serology,serpent,serum,servant,serve,service,servile,servitor,ses
ame,session,set,setback,setscrew,settle,setup,seven,seventh,seventy,sever,several,severe,sew,sewage,sewerage,sewn,shack,shackle,shad,shade,shadow,shad
owy,shady,shag,shaggy,shah,shake,shaken,shaky,shale,shall,shallot,shallow,shamble,shame,shampoo,shamrock,shape,shard,share,shareown,shark,sharp,sharpe
n,shatter,shave,shaven,shaw,shawl,shay,she,sheaf,shear,sheath,sheathe,sheave,shed,sheen,sheep,sheer,sheet,sheik,shelf,shell,shelter,shelve,shepherd,sh
erbet,sheriff,shied,shield,shift,shin,shinbone,shine,shingle,shiny,ship,shiplap,shipman,shipmate,shipmen,shipyard,shire,shirk,shirt,shiver,shoal,shock
,shod,shoe,shoehorn,shoelace,shoemake,shone,shoofly,shook,shop,shopkeep,shore,short,shortage,shortcut,shorten,shortish,shotbush,should,shoulder,s
hout,shove,shovel,show,showboat,showcase,showdown,showman,showmen,shown,showroom,shrank,shrapnel,shred,shrew,shrewd,shriek,shrift,shrike,shrill,shrill
y,shrine,shrink,shrive,shrivel,shroud,shrove,shrub,shrug,shrunk,shrunken,shuck,shudder,shuddery,shuffle,shun,shunt,shut,shutdown,shutoff,shutout,shutt
le,shy,sibilant,sibling,sickle,side,sideband,sidecar,sideline,sidelong,sideman,sidemen,sidereal,siderite,sideshow,sidestep,sidewalk,sidewall,sideway,s
idewise,sidle,siege,sienna,sierra,siesta,sieve,sift,sigh,sight,sightsee,sign,signal,signet,signify,signpost,silage,silane,silent,silica,silicate,silic
on,silk,silken,silkworm,silky,sill,silly,silo,silt,silty,silver,silvery,similar,simile,simmer,simper,simple,simplex,simplify,simply,simulate,since,sin
cere,sinew,sinewy,sinful,sing,singable,singe,single,singlet,singsong,singular,sink,sinkhole,sinter,sinuous,sinus,sinusoid,sip,sir,sire,siren,sister,si
t,site,situate,six,sixfold,sixgun,sixteen,sixth,sixtieth,sixty,size,sizzle,skat,skate,skater,skeletal,skeleton,skeptic,sketch,sketchy,skew,ski,s
kid,skiddy,skied,skiff,skill,skillet,skillful,skim,skimp,skip,skirmish,skirt,skit,skittle,skulk,skull,skullcap,skunk,sky,skyhook,skylark,skylig
ht,skyline,skyward,skywave,skyway,slab,slag,slam,slander,slant,slap,slash,slat,slate,slater,sled,sledge,sleek,sleep,sleet,sleeve,sleigh,sleight,slende
r,slept,sleuth,slew,slice,slick,slid,slide,slight,slim,slime,sling,slip,slippage,slippery,slither,sliver,slivery,slob,sloe,slog,slogan,slop,slope,slos
h,slot,sloth,slouch,slough,sloven,slow,slowdown,sludge,slumber,small,smaller,smallpox,smart,smash,smatter,smear,smelt,smile,smirk,smith,smithy,smitten
,smog,smoke,smoky,smolder,smooth,smother,smudge,snack,snag,snagging,snail,snake,snap,snapback,snappish,snappy,snapshot,snare,snark,snarl,snatch,snazzy
,sneak,sneaky,sneer,sneeze,snell,snick,snippet,snippy,snivel,snook,snoop,snoopy,snore,snorkel,snort,snout,snow,snowball,snowfall,snowshoe,snowy,snub,s
nuffer,snuffle,snuffly,snug,snuggle,snuggly,snyaptic,so,soak,soap,soapsud,soapy,soar,sob,soccer,sociable,social,societal,society,sock,socket,sockeye,s
od,soda,sodden,sodium,sofa,soffit,soft,softball,soften,software,softwood,soggy,soil,soiree,sojourn,solace,solar,sold,solder,soldier,soldiery,sole,sole
cism,solemn,solenoid,solicit,solid,solidify,solidus,solitary,soliton,solitude,solo,solstice,soluble,solute,solution,solvate,solve,solvent,soma,somal,s
omatic,somber,sombre,some,somebody,someday,somehow,someone,sometime,somewhat,son,sonant,sonar,sonata,song,songbag,songbook,songful,sonic,sonnet,sonogr
am,sonority,sonorous,soon,soot,sooth,soothe,soothsay,sop,soprano,sorcery,sordid,sore,sorption,sorrel,sorrow,sorry,sort,souffle,sough,sought,soul,soulf
ul,sound,soup,sour,source,sourwood,soutane,south,southern,souvenir,sow,sown,soy,soybean,spa,space,spacious,spade,spalding,span,spandrel,spangle,spanie
l,spar,spare,sparge,spark,sparkle,sparky,sparling,sparrow,sparse,spasm,spat,spate,spatial,spatlum,spatula,spavin,spawn,speak,spear,spec,special,specie
s,specific,specify,specimen,specious,speck,speckle,spectra,spectral,spectrum,specular,sped,speech,speed,speedup,speedy,spell,spend,spent,sphere,spheri
c,spheroid,spherule,sphinx,spice,spicy,spider,spidery,spigot,spike,spiky,spill,spilt,spin,spinach,spinal,spindle,spine,spinoff,spinster,spiny,spiral,s
pire,spirit,spite,spiteful,splash,splashy,splat,splay,splayed,spleen,splendid,splice,spline,splint,split,splotch,splotchy,splurge,splutter,spoil,spoil
age,spoke,spoken,sponge,spongy,sponsor,spoof,spooky,spool,spoon,spoonful,sporadic,spore,sport,sporty,spot,spotty,spouse,spout,sprain,sprang,sprawl,spr
ay,spread,spree,sprig,spring,springe,springy,sprinkle,sprint,sprite,sprocket,sprout,spruce,sprue,sprung,spumoni,spun,spur,spurge,spurious,spurn,spurt,
sputnik,sputter,spy,spyglass,squabble,squad,squadron,squalid,squall,squamous,squander,square,squash,squawk,squeak,squeaky,squeal,squeegee,squeeze,sque
lch,squid,squill,squint,squire,squirm,squirmy,squirrel,squirt,squishy,stab,stabile,stable,staccato,stack,stadia,stadium,staff,stag,stage,stagnant,stag
nate,stagy,staid,stain,stair,stairway,stake,stale,stalk,stall,stallion,stalwart,stamen,stamina,stammer,stamp,stampede,stance,stanch,stand,standard,sta
ndby,standeth,standoff,stannic,stannous,stanza,staph,staple,star,starch,starchy,stardom,stare,starfish,stargaze,stark,starlet,starling,start,startle,s
tartup,stash,state,stater,static,stator,statuary,statue,stature,status,statute,staunch,stave,stay,stayed,stead,steady,steak,stealth,stealthy,steam,ste
amy,stearate,stearic,steed,steel,steely,steep,steepen,steeple,steer,stein,stellar,stem,stencil,step,stepson,stepwise,stereo,sterile,sterling,stern,ste
rnal,sternum,steroid,stew,steward,stick,stickle,stickpin,sticky,stifle,stigma,stigmata,stile,stiletto,still,stilt,stimuli,stimulus,sting,stingy,stint,
stipend,stipple,stir,stirrup,stitch,stock,stockade,stocky,stodgy,stoic,stoke,stolid,stone,stony,stood,stoop,stop,stopband,stopgap,stopover,sto
ppage,storage,store,stork,storm,stormy,story,stout,stove,stow,stowage,stowaway,straddle,strafe,straggle,straight,strain,strait,strand,strange,strangle
,strap,strata,strategy,stratify,stratum,straw,stray,streak,stream,street,strength,stress,stretch,strewn,striate,stricken,strict,stricter,stride,stride
nt,strife,strike,string,stringy,stripe,stripy,strive,striven,strobe,strode,stroke,stroll,strong,strop,strophe,strove,struck,struggle,strum,strung,stru
t,stub,stubble,stubborn,stubby,stucco,stuck,student,studio,studious,study,stuff,stuffy,stultify,stumble,stump,stumpage,stun,stung,stunt,stupefy,sturdy
,sturgeon,style,styli,stylish,stylites,stylus,stymie,styrene,suave,sub,subject,submit,subpoena,subsidy,subsist,subsume,subsumed,subtle,subtlety,subtly
,suburb,suburbia,subvert,succeed,success,succinct,such,sucrose,sudden,sue,suffer,suffice,suffix,suffrage,suffuse,sugar,suggest,suit,suitcase,suite,sui
tor,sulfa,sulfate,sulfide,sulfite,sulfur,sulfuric,sulk,sulky,sullen,sully,sulphur,sultan,sultry,sum,sumac,summand,summary,summate,summit,summitry,summ
on,sun,sunbeam,sunburn,sunburnt,sunder,sundew,sundial,sundown,sundry,sunfish,sung,sunk,sunken,sunlight,sunlit,sunny,sunrise,sunset,sunshade,sunshine,s
unspot,suntan,sup,super,superb,superbly,superior,supine,supplant,supple,supply,support,suppose,suppress,supreme,supremum,surcease,sure,surety,surf,sur
face,surfeit,surge,surgeon,surgery,surgical,surmise,surmount,surname,surpass,surplus,surprise,surreal,surrey,surround,surtax,surtout,survey,surveyor,s
urvival,survive,survivor,sushi,suspect,suspend,suspense,sustain,suture,svelte,swab,swage,swain,swam,swamp,swan,swanlike,swap,swarm,swart,swarthy,swat,
swatch,swathe,sway,sweat,sweater,sweaty,sweep,sweet,sweeten,sweetish,swell,swelt,swelter,swept,swerve,swift,swim,swimsuit,swindle,swing,swipe,swirl,sw
irly,swiss,switch,swivel,swizzle,swollen,swoop,sword,swore,sworn,swum,swung,sybarite,sycamore,syllabi,syllabic,syllable,syllabus,sylvan,symbol,symboli
c,symmetry,sympathy,symphony,symposia,symptom,synapse,synapses,synaptic,syndrome,synergy,synonym,synonymy,synopses,synopsis,synoptic,syntax,syringa,sy
ringe,syrinx,syrup,syrupy,system,systemic,tab,table,tablet,tabloid,tabular,tabulate,tachinid,tacit,tack,tackle,tacky,tact,tactful,tactic,tactile,tactu
al,tad,tadpole,taffeta,taffy,taft,tag,tagging,tail,tailgate,tailor,tailspin,tailwind,taint,take,taken,takeoff,takeover,taketh,talc,talcum,tale,talent,
talisman,talk,tall,tallow,tally,talon,tamarack,tamarind,tame,tamp,tan,tanager,tandem,tangent,tangible,tangle,tango,tangy,tank,tannin,tansy,tantalum,ta
ntrum,tap,tape,taper,tapestry,tar,tardy,target,tariff,tarnish,tarpaper,tarpon,tarry,tartar,task,tassel,taste,tasteful,tasting,tasty,tat,tate,tater,tat
tle,taught,taunt,taut,tavern,taverna,tawdry,tawny,tax,taxation,taxi,taxicab,taxied,taxiway,taxonomy,taxpayer,tea,teacart,teach,teacup,teahouse,
teakwood,teal,team,teammate,teamster,teamwork,teapot,tear,teardrop,tearful,tease,teasel,teaspoon,tectonic,tedious,tedium,tee,teeing,teem,teen,teenage,
teeter,teeth,tektite,telegram,teleost,telethon,teletype,televise,tell,teller,telltale,temerity,temper,tempera,tempest,template,temple,tempo,temporal,t
empt,ten,tenable,tenacity,tenant,tend,tendency,tendon,tenement,tenet,tenfold,tennis,tenon,tenor,tense,tensile,tension,tensor,tenspot,tent,tentacle,ten
th,tenuous,tenure,tepee,tepid,terbium,tercel,term,terminal,termini,terminus,termite,tern,ternary,terrace,terrain,terrapin,terrible,terrier,terrific,te
rry,terse,tertiary,test,testate,testbed,testify,testy,tetanus,tete,tether,text,textbook,textile,textual,textural,texture,thallium,than,thank,thankful,
that,thatch,thaw,the,theatric,thee,theft,their,theism,theist,them,thematic,theme,then,thence,theology,theorem,theorist,theory,therapy,there,thereat,th
ereby,therein,thereof,thereon,thereto,thermal,these,theses,thesis,they,thiamin,thick,thicken,thicket,thigh,thimble,thin,thine,thing,think,thinnish,thi
rd,thirst,thirsty,thirteen,thirty,this,thistle,thither,thong,thoriate,thorium,thorn,thorny,thorough,those,thou,though,thought,thousand,thrall,thread,t
hree,thresh,threw,thrice,thrift,thrifty,thrill,thrive,throes,throne,throng,throttle,through,throw,thrown,thrum,thrush,thud,thulium,thumb,thump,thunder
,thus,thwart,thy,thyme,thymine,thymus,thyroid,tibia,tick,ticket,tickle,ticklish,tidal,tidbit,tide,tideland,tidy,tie,tied,tier,tift,tiger,tight,tighten
,tigress,til,tilde,tile,till,tilt,tilth,timber,timbre,time,timeworn,timid,tin,tinder,tine,tinfoil,tinge,tingle,tinker,tinkle,tinsel,tint,tintype,tiny,
tip,tipoff,tippy,tiptoe,tirade,tire,tiresome,tissue,titanate,titanic,titanium,tithe,titian,title,to,toad,toady,toast,today,toe,toenail,toffee,tofu,tog
ether,togging,toggle,toil,toilsome,token,told,tolerant,tolerate,toll,tollgate,toluene,tomato,tomatoes,tome,tomorrow,ton,tonal,tone,tong,tonic,tonight,
tonnage,tonsil,too,took,tool,toolkit,toolmake,toot,tooth,top,topaz,topcoat,topic,topmost,topnotch,topology,topple,topsoil,torch,tore,torn,tornado,toro
id,toroidal,torpedo,torpid,torpor,torque,torrent,torrid,torsion,torso,tort,tortoise,toss,tot,total,tote,totem,totemic,touch,tough,tour,tousle,tout,tow
,toward,towboat,towel,tower,town,townsman,townsmen,toxic,toxin,toy,trace,tracery,trachea,track,trackage,tract,tractor,trade,tradeoff,traffic,tragedy,t
ragic,trail,train,trainee,trainman,trainmen,traipse,trait,traitor,tram,trammel,trample,tramway,trance,tranquil,transact,transect,transept,transfer,tra
nsfix,transit,transmit,transom,travail,travel,traverse,travesty,trawl,tray,tread,treadle,treason,treasure,treasury,treat,treatise,treaty,treble,tree,t
reetop,trefoil,trek,trellis,tremble,tremor,trench,trend,trendy,trespass,tress,trestle,triad,trial,triangle,tribal,tribe,tribunal,tribune,tribute,trick
,trickery,trickle,tricky,trident,tried,trifle,trig,trigonal,trigram,trill,trillion,trilogy,trim,trimer,trinity,trinket,trio,triode,trioxide,trip,tripe
,triple,triplet,triplex,tripod,tripoli,triptych,tristate,trite,tritium,triton,triumph,triune,trivia,trivial,trivium,trod,trodden,troika,troll,trolley,
trollop,trombone,trompe,troop,trophic,trophy,tropic,trot,trough,trounce,troupe,trouser,trout,troy,truancy,truant,truce,truck,trudge,truism,truly,trump
,trumpery,trumpet,truncate,trundle,trunk,truss,trust,trustee,trustful,truth,truthful,try,trypsin,tsunami,tub,tuba,tube,tubular,tubule,tuck,tuff,tuft,t
ug,tugging,tuition,tulip,tulle,tumble,tumbrel,tumult,tuna,tundra,tune,tuneful,tungsten,tunic,tunnel,tupelo,tuple,turban,turbid,turbine,turbofan,turboj
et,turf,turgid,turk,turkey,turmoil,turn,turnery,turnip,turnkey,turnoff,turnout,turnpike,turret,turtle,turvy,tusk,tussle,tutelage,tutor,tutorial,tutu,t
uxedo,twain,tweak,tweed,tweedy,tweeze,twelfth,twelve,twenty,twice,twiddle,twig,twigging,twilight,twill,twin,twine,twinge,twinkle,twirl,twirly,twist,tw
isty,twitch,twitchy,two,twofold,twosome,tycoon,tying,type,typeface,typeset,typhoid,typhoon,typhus,typic,typify,typo,typology,tyrannic,tyranny,tyrosine
,ubiquity,ulterior,ultimate,ultra,umber,umbra,umbrage,umbrella,umlaut,umpire,unary,unbidden,uncle,unction,under,undulate,uniaxial,unicorn,uniform,unif
y,unimodal,union,uniplex,unipolar,unique,unison,unitary,unite,unity,universe,until,unwieldy,up,upbeat,upbraid,upbring,upcome,update,updraft,upend,upgr
ade,upheaval,upheld,uphill,uphold,upkeep,upland,uplift,upon,upper,uppercut,upraise,upright,uprise,upriver,uproar,uproot,upset,upshot,upside,upsilon,up
slope,upstair,upstand,upstart,upstate,upstater,upstream,upsurge,upswing,uptake,uptown,uptrend,upturn,upward,upwind,uranium,urban,urbane,urbanite,urchi
n,urge,urgency,urgent,urging,us,usable,usage,use,useful,usher,usual,usurer,usurious,usurp,usury,utensil,utility,utmost,utopia,utopian,utter,vacant,vac
ate,vaccine,vacua,vacuo,vacuole,vacuous,vacuum,vade,vagabond,vagary,vagrant,vague,vain,vale,valent,valet,valeur,valiant,valid,validate,valine,valley,v
aluate,value,valve,vamp,vampire,van,vanadium,vandal,vane,vanguard,vanilla,vanish,vanity,vanquish,vantage,vapid,vaporous,variable,variac,variant,variat
e,variety,various,varistor,varnish,varsity,vary,vascular,vase,vassal,vast,vat,vault,vaunt,veal,vector,vee,veer,veery,vegetate,vehicle,veil,vein,velar,
veldt,vellum,velocity,velours,velvet,velvety,venal,vend,vendetta,vendible,vendor,veneer,venerate,venial,venous,vent,venture,venturi,veracity,veranda,v
erandah,verb,verbal,verbatim,verbena,verbiage,verbose,verdant,verdict,verge,veridic,verify,verity,vermeil,vernal,vernier,versa,versate
c,verse,version,versus,vertebra,vertex,vertical,vertices,vertigo,verve,very,vesper,vessel,vest,vestal,vestige,vestry,vet,vetch,veteran,veto,via,viaduc
t,vial,vibrant,vibrate,vibrato,viburnum,vicar,viceroy,vicinal,vicinity,vicious,victor,victory,victrola,victual,vide,video,vie,view,vigil,vigilant,vign
ette,vigorous,villa,village,villain,villein,vine,vinegar,vineyard,vintage,vintner,vinyl,viola,violate,violent,violet,violin,virgule,virtual,virtue,vir
tuosi,virtuoso,virtuous,virulent,virus,vis,visa,visage,viscount,viscous,vise,visible,vision,visit,visitor,visor,vista,visual,vita,vitae,vital,vitamin,
vitiate,vitreous,vitrify,vitriol,vitro,viva,vivace,vivacity,vivid,vivify,vivo,vixen,viz,vocable,vocal,vocalic,vocate,vogue,voice,void,volatile,volcani
c,volcano,volition,volley,volt,voltage,voltaic,voluble,volume,voracity,vortex,vortices,votary,vote,votive,vouch,vow,vowel,voyage,vulgar,vulpi,vulture,
vying,wacky,wad,waddle,wade,wafer,waffle,wag,wage,wagging,waggle,wagoneer,wail,waist,wait,waitress,waive,wake,wakeful,waken,wakeup,wale,walk,walkout,w
alkover,walkway,wall,wallaby,wallet,wallop,wallow,wally,walnut,walrus,waltz,waltzing,wan,wand,wander,wane,wangle,want,wanton,wapato,wapiti,war,warble,
ward,warden,wardrobe,wardroom,ware,warm,warmth,warmup,warn,warp,warplane,warrant,warranty,warren,warrior,wary,was,wash,washbowl,washout,washy,was
pish,wast,wastage,wasteful,wastrel,watch,watchdog,watchful,watchman,watchmen,water,waterway,watery,watt,wattage,wattle,wave,waveform,wavelet,wavy,wax,
waxen,waxwork,waxy,way,waybill,waylaid,waylay,wayside,wayward,we,weak,weaken,weal,wealth,wealthy,wean,wear,wearied,weary,weasel,weather,weave,web,webe
r,wed,wedge,wedlock,wee,week,weekday,weekend,weep,weigh,weight,weighty,weir,welcome,weld,well,welsh,welt,went,wept,were,wert,
west,westerly,western,westward,wet,wetland,whale,wharf,wharves,what,whatever,whatnot,wheat,wheedle,wheel,wheeze,wheezy,whelk,whelm,when,whence,wheneve
r,where,whereas,whereby,wherein,whereof,whereon,wherever,whet,whether,which,whig,while,whim,whimsey,whimsic,whir,whirl,
whish,whisk,whisper,whistle,whit,white,whiten,whither,whittle,who,whoever,whole,wholly,whom,whomever,whose,why,wick,wicket,wide,widen,widgeon,widget,w
idow,width,wield,wig,wigging,wiggle,wildcat,wildfire,wildlife,will,willow,willowy,wilt,win,wind,windfall,w
indmill,window,windsurf,windup,windward,windy,wing,wingback,wingman,wingmen,wingspan,wingtip,wink,winkle,winnow,winsome,winter,wintry,winy,wipe,wire,w
ireman,wiremen,wiretap,wiry,wisdom,wise,wish,wishbone,wishful,wisp,wispy,wistful,wit,with,withal,withdraw,withdrew,withe,wither,withheld,withhold,with
in,without,withy,witness,witty,wizard,wobble,woe,woeful,wok,woke,wold,wolf,wolfish,wolves,woman,wombat,women,won,wonder,wondrous,wont,woo,woodcut
,wooden,woodhen,woodland,woodlot,woodpeck,woodrow,woodruff,woodshed,woodside,woodward,woodwind,woodwork,wool,word,wordy,wore,work,workaday,workbook,wo
rkday,workload,workman,workmen,workout,workshop,world,worm,worn,worth,worthy,would,wound,wove,woven,wow,wrack,wraith,wrangle,wrap,wrapup,wrathfu
l,wreak,wreath,wreathe,wreck,wreckage,wrench,wrest,wrestle,wretch,wriggle,wright,wring,wrinkle,wrist,writ,write,writeup,writhe,written,wrote,wrought,w
ry,xenon,xylem,xylene,yacht,yak,yam,yap,yapping,yard,yardage,yarmulke,yarn,yarrow,yawn,yea,yeah,year,yearbook,yearn,yeast,yell,yelp,yeoman,
yeomanry,yet,yield,yip,yipping,yodel,yoga,yogi,yogurt,yoke,yolk,yon,yond,yore,you,young,youngish,your,yourself,youth,youthful,yule,zag,zagging
,zap,zazen,zeal,zealot,zealous,zebra,zenith,zero,zeroes,zeroth,zest';

  -- 02/06/2015 - updated words lists per program to remove vulgar words
  VULGAR CONSTANT VARCHAR2(1200) := 'DAMN,DIC,FART,LUST,SEX,SHIT,SUC,FUC,TIT,ASS,PRIC,COC,DORK,FAG,CUNT,BOOB,LICK,ABATER,ADOPT,ADOPTION,AIRMASS,AMASS,ANGEL,ANISE,APE,
  ARROGANT,ATE,BACON,BANAL,BAREFOOT,BATH,BATHE,BEDBUG,BEDEVIL,BEGGAR,BELLY,BIOPSY,BLIND,BONE,BROWN,CAROUSE,CONCH,COQUETTE,CROSS,DERVISH,DRAMA,EMIGRANT,EMIGRATE,ENDOGAMY,ETHIC,
  FETISH,FINGER,FIRE,FIST,FLIRT,FREE,GRUESOME,HANDCUFF,HOSTAGE,HOSTILE,HUSKY,HUSTLE,INMATE,INNUENDO,JOB,JUMBO,LIMP,LITTORAL,MINE,NAVEL,NOB,OOZE,PECK,PIG,
  PIPE,POKE,PORCINE,POTBELLY,PREGNANT,RIM,SEIZURE,SHOT,SKEET,SKINNY,SLENDER,STOMACH,TATTOO,VERMIN,VERMOUTH,VULGAR,WARPLANE,WARRIOR,WASP,WEED,WEEDY,WEIRD,
  WELFARE,WHIFF,WHIMPER,WHIPLASH,WHIPSAW,WILD,WILE,WILFUL,WILLFUL,WILY,WINCE,WINDBAG,WOOD,WRATH,WRATHFUL,YELLOW,YEN,YOGHURT';
  NEGATIVE CONSTANT VARCHAR2(500) := 'DUMB,FAIL,LOSE,FLUNK';
  NINES    CONSTANT VARCHAR2(25) := '999999999999999999999999';

  FUNCTION PROGRAMIDFORCUSTOMERNPRODUCT(CUSTOMERID NUMBER,
                                        PRODUCTID  NUMBER,
                                        STARTDATE  DATE) RETURN NUMBER;
  FUNCTION ISTESTRESTRICTEDFORSTUDENT(STUDENTID     NUMBER,
                                      TESTITEMSETID NUMBER) RETURN NUMBER;
  FUNCTION GETTESTSESSIONSTATUS(VCONFIGDATA TEST_SCHEDULE_CONFIG%ROWTYPE)
    RETURN VARCHAR2;
  FUNCTION GENERATEPASSWORD(MIN_LENGTH   NUMBER,
                            MAX_LENGTH   NUMBER,
                            ISACCESSCODE NUMBER) RETURN VARCHAR2;
  PROCEDURE LOG_MESSAGE(P_ID            INTEGER,
                        P_STATUS        IN VARCHAR2,
                        P_ERROR_MESSAGE IN VARCHAR2);
  PROCEDURE PROCESS_EX_CREATE_TEST_SESSION(VCUSTOMERID    INTEGER,
                                     VPRODUCTID     INTEGER,
                                     SCHEDULINGTYPE VARCHAR2,
                                     INTESTCATALOGID INTEGER);

  PROCEDURE PROCESS_IN_CREATE_TEST_SESSION(VCUSTOMERID    INTEGER,
                                        VPRODUCTID     INTEGER,
                                        SCHEDULINGTYPE VARCHAR2,
                                        INTESTCATALOGID INTEGER);
END IREAD3_PRESCHEDULING_2015_SUMR;
/
CREATE OR REPLACE PACKAGE BODY IREAD3_PRESCHEDULING_2015_SUMR IS

  -- Private constant declarations
   /*Prescheduling story 1549 - Start Changes*/
  /*OPERATIONAL_PRODUCT_ID    CONSTANT PRODUCT.PRODUCT_ID%TYPE := 6103;
  PRACTICE_PRODUCT_ID       CONSTANT PRODUCT.PRODUCT_ID%TYPE := 6106;
  IREAD_PRACTICE_PRODUCT_ID CONSTANT PRODUCT.PRODUCT_ID%TYPE := 6108;
  IREAD_OPRTIONL_PRODUCT_ID CONSTANT PRODUCT.PRODUCT_ID%TYPE := 6109;
  SCIENCE_PRODUCT_ID        CONSTANT PRODUCT.PRODUCT_ID%TYPE := 6110;
  V_TEST_CATALOG_ID         CONSTANT TEST_CATALOG.TEST_CATALOG_ID%TYPE := 88788;*/
  /*Prescheduling story 1549 - End Changes*/

  SCHOOL_CATEGORY_LEVEL CONSTANT INTEGER := 3;
  DEFAULT_CREATED_BY    CONSTANT INTEGER := 2;
  TEST_TIME_EXPIRED     CONSTANT VARCHAR2(1) := 'F';
  --TEST_ADMIN
  V_ACTIVATION_STATUS    CONSTANT VARCHAR2(2) := 'AC';
  ENFORCE_BREAK          CONSTANT VARCHAR2(1) := 'T';
  TEST_ADMIN_TYPE        CONSTANT VARCHAR2(2) := 'SE';
  ENFORCE_TIME_LIMIT     CONSTANT VARCHAR2(1) := 'T';
  FORM_ASSIGNMENT_METHOD CONSTANT VARCHAR2(10) := 'roundrobin';
  PREFERRED_FORM         CONSTANT TEST_ADMIN.PREFERRED_FORM%TYPE := '1';
  --TEST_ADMIN_ITEM_SET

  --TEST_ADMIN_USER_ROLE
  ACC_MANAGER_ROLE_ID   CONSTANT INTEGER := 1004;
  ADMINISTRATOR_ROLE_ID CONSTANT INTEGER := 1005;

  --TEST_ROSTER
  TEST_COMPLETION_STATUS CONSTANT VARCHAR2(2) := 'SC';
  VALIDATION_STATUS      CONSTANT VARCHAR2(2) := 'VA';
  OVERRIDE_TEST_WINDOW   CONSTANT VARCHAR2(1) := 'F';
  SCORING_STATUS         CONSTANT VARCHAR2(2) := 'NA';
  V_FORM_ASSIGNMENT      CONSTANT TEST_ROSTER.FORM_ASSIGNMENT%TYPE := 1;
  V_LOG_ID INTEGER := TO_NUMBER(TO_CHAR(SYSDATE, 'YYYY') ||
                                TO_CHAR(SYSDATE, 'MM') ||
                                TO_CHAR(SYSDATE, 'DD') ||
                                TO_CHAR(SYSDATE, 'hh24') ||
                                TO_CHAR(SYSDATE, 'MI') ||
                                TO_CHAR(SYSDATE, 'SS'));
  -- LOG STATUS ;
  V_STATUS_SUCCESS  CONSTANT VARCHAR2(10) := 'SUCCESS';
  V_STATUS_WARN     CONSTANT VARCHAR2(10) := 'WARNING';
  V_STATUS_ERROR    CONSTANT VARCHAR2(10) := 'FAILURE';
  V_STATUS_COMPLETE CONSTANT VARCHAR2(10) := 'COMPLETE';
  V_STATUS_START    CONSTANT VARCHAR2(10) := 'STARED';

  --passwor,accesscode

  V_STD_PASS_MIN_LENGTH          CONSTANT NUMBER(2) := 6;
  V_STD_PASS_MAX_LENGTH          CONSTANT NUMBER(2) := 8;
  V_GENERATED_ACCESS_CODE_LENGTH CONSTANT NUMBER(2) := 10;

  NODE_EXCLUDED       INTEGER;
  NODE_INCLUDED       INTEGER;
  VROSTER_STATUS_FLAG VARCHAR2(300) := 'Roster_Status_Flag';
  TYPE ARRAY_OF_STR IS TABLE OF VARCHAR2(2) INDEX BY BINARY_INTEGER;

  V_ARRAY6_8   ARRAY_OF_STR; --array for grade 6,7,8
  V_ARRAY_FORM ARRAY_OF_STR; --array for form

  /*
    Retrieve all Item_setId of type TC associated to given product of ISTEP customer
  */

  CURSOR CRSGETTCTESTFORPRODUCT(VPRODUCTID INTEGER, VUSERID USERS.USER_ID%TYPE, INTESTCATALOGID INTEGER) IS
    SELECT DISTINCT ISET.ITEM_SET_ID,
                    ISET.ITEM_SET_DISPLAY_NAME,
                    ISET.GRADE,
                    MAX(ONTC.RANDOM_DISTRACTOR_ALLOWABLE) AS RANDOM_DISTRACTOR_ALLOWABLE,
                    MAX(ONTC.OVERRIDE_FORM_ASSIGNMENT) AS OVERRIDE_FORM_ASSIGN_METHOD
      FROM PRODUCT PROD,
           ITEM_SET_PRODUCT ISP,
           ITEM_SET ISET,
           ORG_NODE_TEST_CATALOG ONTC,
           USER_ROLE UROLE,
           USERS
     WHERE PROD.ACTIVATION_STATUS = V_ACTIVATION_STATUS
       AND ISET.ACTIVATION_STATUS = V_ACTIVATION_STATUS
       AND ISP.PRODUCT_ID = PROD.PRODUCT_ID
       AND ISET.ITEM_SET_ID = ISP.ITEM_SET_ID
       AND ISET.ITEM_SET_TYPE = 'TC'
       AND PROD.PRODUCT_ID = VPRODUCTID
       AND ONTC.TEST_CATALOG_ID = INTESTCATALOGID
       AND ONTC.ITEM_SET_ID = ISET.ITEM_SET_ID
       AND ONTC.ACTIVATION_STATUS = V_ACTIVATION_STATUS
       AND UROLE.ORG_NODE_ID = ONTC.ORG_NODE_ID
       AND UROLE.ACTIVATION_STATUS = V_ACTIVATION_STATUS
       AND USERS.USER_ID = UROLE.USER_ID
       AND USERS.USER_ID = VUSERID
     GROUP BY ISET.ITEM_SET_ID,
              ISET.ITEM_SET_DISPLAY_NAME,
              ISET.ACTIVATION_STATUS,
              ISET.GRADE;

  CURSOR CRSGETSTUFORALLGRADEATSCHOOL(VGRADE ITEM_SET.GRADE%TYPE, VSCHOOLORGNODEID ORG_NODE_ANCESTOR.ORG_NODE_ID%TYPE, VPRODUCTID INTEGER, INTESTCATALOGID INTEGER) IS
    SELECT STU.STUDENT_ID,
           STU.USER_NAME,
           STU.ACTIVATION_STATUS,
           STU.GRADE,
           MAX(ONS.ORG_NODE_ID) AS ORG_NODE_ID

      FROM ORG_NODE_STUDENT ONS, STUDENT STU, ORG_NODE NODE
     WHERE ONS.STUDENT_ID = STU.STUDENT_ID
       AND ONS.ACTIVATION_STATUS = V_ACTIVATION_STATUS
       AND STU.ACTIVATION_STATUS = V_ACTIVATION_STATUS
       AND STU.GRADE = VGRADE
          -- AND STU.PRECODE_ID IS NOT NULL -- Can be commented to use procedure for scheduling all type of student(UI and Informatica)
       AND NVL(STU.OUT_OF_SCHOOL, 'No') <> 'Yes'
       AND ONS.ORG_NODE_ID = NODE.ORG_NODE_ID
       AND ONS.ORG_NODE_ID IN
           (SELECT ORG_NODE_ID
              FROM ORG_NODE_ANCESTOR ONS
             WHERE ANCESTOR_ORG_NODE_ID = VSCHOOLORGNODEID
               AND NUMBER_OF_LEVELS IN (0, 1))
       AND DECODE(VPRODUCTID,
                  6106,
                  1,
                  6107,
                  1,
                  6108,
                  1,
                  6112,
                  1,
                  6119,
                  1,
                  /*6118,
                                                                          1,*/

                  (SELECT DECODE(COUNT(*), 0, 1, 0)
                     FROM TEST_ADMIN ADM, TEST_ROSTER ROS
                    WHERE ROS.STUDENT_ID = STU.STUDENT_ID
                      AND ROS.TEST_ADMIN_ID = ADM.TEST_ADMIN_ID
                      AND ADM.ACTIVATION_STATUS = 'AC'
                      --AND ADM.TEST_CATALOG_ID = V_TEST_CATALOG_ID -- ADDED FOR IREAD SUMMER TEST on 21.05.2014
                      --Changed for Prescheduling story 1549
                      AND ADM.PRODUCT_ID = VPRODUCTID
                      AND ADM.TEST_CATALOG_ID = INTESTCATALOGID)) = 1
       AND STU.SPN_5 = '22222222' ---> added for IREAD 3 Retest Test Prescheduling
     GROUP BY STU.STUDENT_ID,
              STU.ACTIVATION_STATUS,
              STU.USER_NAME,
              STU.GRADE;

  CURSOR CRSGETSCHEDULABLEUNITSFORTEST(VPARENTITEMSETID ITEM_SET.ITEM_SET_ID%TYPE) IS
    SELECT ISET.ITEM_SET_ID
      FROM ITEM_SET ISET, ITEM_SET_PARENT ISP
     WHERE ISET.ACTIVATION_STATUS = V_ACTIVATION_STATUS
       AND ISET.ITEM_SET_ID = ISP.ITEM_SET_ID
       AND ISET.ITEM_SET_TYPE = 'TS'
       AND ISP.PARENT_ITEM_SET_ID = VPARENTITEMSETID /*{parentItemSetId}-- TC ItemsetId*/
     ORDER BY ISP.ITEM_SET_SORT_ORDER;

  CURSOR CRSGETTDTESTELEMENTLIST(VPARENTITEMSETID ITEM_SET.ITEM_SET_ID%TYPE, VFORM ITEM_SET.ITEM_SET_FORM%TYPE) IS
    SELECT ISET.ITEM_SET_ID
      FROM ITEM_SET ISET, ITEM_SET_PARENT ISP
     WHERE ISET.ACTIVATION_STATUS = V_ACTIVATION_STATUS
       AND ISET.ITEM_SET_ID = ISP.ITEM_SET_ID
       AND ISET.ITEM_SET_TYPE = 'TD'
       AND ISP.PARENT_ITEM_SET_ID = VPARENTITEMSETID
       AND ISET.ITEM_SET_FORM = VFORM
     ORDER BY ISP.ITEM_SET_SORT_ORDER;
  ------Added for form assignment implementation

  CURSOR CRSGETFORMSASSIGNMENT(VTESTADMINID TEST_ADMIN.TEST_ADMIN_ID%TYPE) IS
    SELECT ISET.ITEM_SET_FORM AS FORM
      FROM TEST_ADMIN ADM,
           ITEM_SET ISET,
           ITEM_SET_ANCESTOR ISA,
           (SELECT DISTINCT TEST_ROSTER_ID, FORM_ASSIGNMENT
              FROM TEST_ROSTER
             WHERE TEST_ADMIN_ID = VTESTADMINID) ROS
     WHERE ISA.ANCESTOR_ITEM_SET_ID = ADM.ITEM_SET_ID
       AND ISET.ITEM_SET_ID = ISA.ITEM_SET_ID
       AND ROS.FORM_ASSIGNMENT(+) = ISET.ITEM_SET_FORM
       AND ISET.ITEM_SET_TYPE = 'TD'
       AND ADM.TEST_ADMIN_ID = VTESTADMINID
     GROUP BY ISET.ITEM_SET_FORM;

  TYPE ARRSTUFORALLGRADEATSCHOOL IS TABLE OF CRSGETSTUFORALLGRADEATSCHOOL%ROWTYPE;
  V_STUDENT          CRSGETSTUFORALLGRADEATSCHOOL%ROWTYPE;
  V_ARRAY_OF_STUDENT ARRSTUFORALLGRADEATSCHOOL;

  -- getting a random no. of desired length
  FUNCTION GET_FIXED_LENGTH_RAND_NO(NO_OF_DIGITS NUMBER) RETURN NUMBER IS
    V_RANDOM_NO   NUMBER;
    V_NO_OF_NINES VARCHAR2(25);
  BEGIN
    V_NO_OF_NINES := SUBSTR(NINES, 1, NO_OF_DIGITS);
    V_RANDOM_NO   := ABS(MOD(DBMS_RANDOM.RANDOM, TO_NUMBER(V_NO_OF_NINES)));
    IF LENGTH(V_RANDOM_NO) < NO_OF_DIGITS THEN
      V_RANDOM_NO := GET_FIXED_LENGTH_RAND_NO(NO_OF_DIGITS);
    END IF;
    RETURN V_RANDOM_NO;
  END;

  FUNCTION GENERATEPASSWORD(MIN_LENGTH   NUMBER,
                            MAX_LENGTH   NUMBER,
                            ISACCESSCODE NUMBER) RETURN VARCHAR2 AS
    V_RAND_NO1      NUMBER(2) := DBMS_RANDOM.VALUE(1, 5);
    V_RAND_NO2      NUMBER(10);
    V_LENGTH        NUMBER(10);
    V_PASS          VARCHAR2(32726);
    V_FIRST_OCCUR   NUMBER(10);
    V_SECOND_OCCUR  NUMBER(10);
    V_MAGNITUDE     VARCHAR2(25);
    VAL1            NUMBER(10);
    V_RECORD_COUNT  NUMBER(3);
    V_RECORD_COUNT1 NUMBER(3);

  BEGIN

    ---------------------------------------------------------
    ------------ GENERATING RENDOM VALUE --------------------
    ---------------------------------------------------------
    CASE V_RAND_NO1
      WHEN 1 THEN
        V_LENGTH      := LENGTH(WORDS1) - LENGTH(REPLACE(WORDS1, ','));
        V_RAND_NO2    := DBMS_RANDOM.VALUE(1, V_LENGTH);
        V_FIRST_OCCUR := (INSTR(WORDS1, ',', 1, V_RAND_NO2));

        IF (V_LENGTH = V_RAND_NO1) THEN
          V_PASS := SUBSTR(WORDS1, V_FIRST_OCCUR + 1);
        ELSE
          V_SECOND_OCCUR := (INSTR(WORDS1, ',', V_FIRST_OCCUR + 1));
          V_PASS         := SUBSTR(WORDS1,
                                   V_FIRST_OCCUR + 1,
                                   V_SECOND_OCCUR - V_FIRST_OCCUR - 1);
        END IF;

      WHEN 2 THEN
        V_LENGTH      := LENGTH(WORDS2) - LENGTH(REPLACE(WORDS2, ','));
        V_RAND_NO2    := DBMS_RANDOM.VALUE(1, V_LENGTH);
        V_FIRST_OCCUR := (INSTR(WORDS2, ',', 1, V_RAND_NO2));
        IF (V_LENGTH = V_RAND_NO1) THEN
          V_PASS := SUBSTR(WORDS2, V_FIRST_OCCUR + 1);
        ELSE
          V_SECOND_OCCUR := (INSTR(WORDS2, ',', V_FIRST_OCCUR + 1));
          V_PASS         := SUBSTR(WORDS2,
                                   V_FIRST_OCCUR + 1,
                                   V_SECOND_OCCUR - V_FIRST_OCCUR - 1);
        END IF;

      WHEN 3 THEN
        V_LENGTH      := LENGTH(WORDS3) - LENGTH(REPLACE(WORDS3, ','));
        V_RAND_NO2    := DBMS_RANDOM.VALUE(1, V_LENGTH);
        V_FIRST_OCCUR := (INSTR(WORDS3, ',', 1, V_RAND_NO2));
        IF (V_LENGTH = V_RAND_NO1) THEN
          V_PASS := SUBSTR(WORDS3, V_FIRST_OCCUR + 1);
        ELSE
          V_SECOND_OCCUR := (INSTR(WORDS3, ',', V_FIRST_OCCUR + 1));
          V_PASS         := SUBSTR(WORDS3,
                                   V_FIRST_OCCUR + 1,
                                   V_SECOND_OCCUR - V_FIRST_OCCUR - 1);
        END IF;

      WHEN 4 THEN
        V_LENGTH      := LENGTH(WORDS4) - LENGTH(REPLACE(WORDS4, ','));
        V_RAND_NO2    := DBMS_RANDOM.VALUE(1, V_LENGTH);
        V_FIRST_OCCUR := (INSTR(WORDS4, ',', 1, V_RAND_NO2));
        IF (V_LENGTH = V_RAND_NO1) THEN
          V_PASS := SUBSTR(WORDS4, V_FIRST_OCCUR + 1);
        ELSE
          V_SECOND_OCCUR := (INSTR(WORDS4, ',', V_FIRST_OCCUR + 1));
          V_PASS         := SUBSTR(WORDS4,
                                   V_FIRST_OCCUR + 1,
                                   V_SECOND_OCCUR - V_FIRST_OCCUR - 1);
        END IF;

      WHEN 5 THEN
        V_LENGTH      := LENGTH(WORDS5) - LENGTH(REPLACE(WORDS5, ','));
        V_RAND_NO2    := DBMS_RANDOM.VALUE(1, V_LENGTH);
        V_FIRST_OCCUR := (INSTR(WORDS5, ',', 1, V_RAND_NO2));
        IF (V_LENGTH = V_RAND_NO1) THEN
          V_PASS := SUBSTR(WORDS5, V_FIRST_OCCUR + 1);
        ELSE
          V_SECOND_OCCUR := (INSTR(WORDS5, ',', V_FIRST_OCCUR + 1));
          V_PASS         := SUBSTR(WORDS5,
                                   V_FIRST_OCCUR + 1,
                                   V_SECOND_OCCUR - V_FIRST_OCCUR - 1);
        END IF;
    END CASE;

    ----------------------------------------------------------
    ------------- REMOVING WHITESPACE CHARACTER---------------
    ----------------------------------------------------------
    SELECT REGEXP_REPLACE(V_PASS, '\s*', '') INTO V_PASS FROM DUAL;

    ---------------------------------------------------------
    ----- CHECKING VULGAR OR NOT FOR PASSWORD ---------------
    ---------------------------------------------------------
    IF (ISACCESSCODE = 0) THEN
      IF ((INSTR(VULGAR, LOWER(V_PASS), 1)) > 0) THEN
        V_PASS := GENERATEPASSWORD(MIN_LENGTH, MAX_LENGTH, ISACCESSCODE);
      ELSIF ((INSTR(NEGATIVE, LOWER(V_PASS), 1)) > 0) THEN
        V_PASS := GENERATEPASSWORD(MIN_LENGTH, MAX_LENGTH, ISACCESSCODE);
      END IF;

    END IF;

    ---------------------------------------------------------
    ----- GENERATING ALFA NUMERIC VALUE ---------------------------
    ---------------------------------------------------------
    V_LENGTH := MIN_LENGTH - LENGTH(V_PASS);

    IF (V_LENGTH >= 1) THEN
      /*V_MAGNITUDE := SUBSTR(NINES, 1, V_LENGTH);
      VAL1        := ABS(MOD(DBMS_RANDOM.RANDOM, TO_NUMBER(V_MAGNITUDE)));*/
      VAL1   := GET_FIXED_LENGTH_RAND_NO(V_LENGTH);
      V_PASS := V_PASS || VAL1;
    ELSE
      IF V_LENGTH = 0 THEN
        --V_PASS := substr(V_PASS, 1, length(V_PASS) - 1) || ABS(MOD(DBMS_RANDOM.RANDOM, 9));
        V_PASS := V_PASS || ABS(MOD(DBMS_RANDOM.RANDOM, 9));
      ELSIF V_LENGTH < 0 THEN
        /*IF ABS(V_LENGTH) <= (MAX_LENGTH - MIN_LENGTH) THEN
           V_PASS := substr(V_PASS, 1, length(V_PASS) - ABS(V_LENGTH) +1) || ABS(MOD(DBMS_RANDOM.RANDOM, 9));
        ELSIF ABS(V_LENGTH) > (MAX_LENGTH - MIN_LENGTH) THEN
           V_PASS := substr(V_PASS, 1, length(V_PASS) - ABS(V_LENGTH) +1) || ABS(MOD(DBMS_RANDOM.RANDOM, 9));
        END IF; */
        V_PASS := substr(V_PASS, 1, length(V_PASS) - ABS(V_LENGTH) + 1) ||
                  ABS(MOD(DBMS_RANDOM.RANDOM, 9));
      END IF;
      /*VAL1   := ABS(MOD(DBMS_RANDOM.RANDOM, 9));
      V_PASS := V_PASS || VAL1;*/
    END IF;

    ---------------------------------------------------------
    ----- VALIDATING ACCESS CODE ---------------------------
    ---------------------------------------------------------

    IF (ISACCESSCODE = 1) THEN
      SELECT COUNT(ACCESS_CODE)
        INTO V_RECORD_COUNT
        FROM TEST_ADMIN
       WHERE upper(ACCESS_CODE) = upper(V_PASS);

      SELECT COUNT(TSIT.ACCESS_CODE)
        INTO V_RECORD_COUNT1
        FROM TEST_ADMIN_ITEM_SET TSIT
       WHERE upper(TSIT.ACCESS_CODE) = upper(V_PASS);

      IF (V_RECORD_COUNT > 0) OR (V_RECORD_COUNT1 > 0) THEN

        V_PASS := GENERATEPASSWORD(MIN_LENGTH, MAX_LENGTH, ISACCESSCODE);
      END IF;
    END IF;

    IF V_PASS IS NULL OR LENGTH(V_PASS) = 0 THEN
      V_PASS := GENERATEPASSWORD(MIN_LENGTH, MAX_LENGTH, ISACCESSCODE);
    END IF;

    RETURN UPPER(V_PASS);
  END GENERATEPASSWORD;

  FUNCTION GENERATE_TAC(DESIREDLENGTH NUMBER, ISACCESSCODE NUMBER)
    RETURN VARCHAR2 AS
    V_RAND_NO1      NUMBER(2) := DBMS_RANDOM.VALUE(1, 5);
    V_RAND_NO2      NUMBER(10);
    V_LENGTH        NUMBER(10);
    V_PASS          VARCHAR2(32726);
    V_FIRST_OCCUR   NUMBER(10);
    V_SECOND_OCCUR  NUMBER(10);
    V_MAGNITUDE     VARCHAR2(25);
    VAL1            NUMBER(10);
    V_RECORD_COUNT  NUMBER(3);
    V_RECORD_COUNT1 NUMBER(3);

  BEGIN

    ---------------------------------------------------------
    ------------ GENERATING RENDOM VALUE --------------------
    ---------------------------------------------------------
    CASE V_RAND_NO1
      WHEN 1 THEN
        V_LENGTH      := LENGTH(WORDS1) - LENGTH(REPLACE(WORDS1, ','));
        V_RAND_NO2    := DBMS_RANDOM.VALUE(1, V_LENGTH);
        V_FIRST_OCCUR := (INSTR(WORDS1, ',', 1, V_RAND_NO2));

        IF (V_LENGTH = V_RAND_NO1) THEN
          V_PASS := SUBSTR(WORDS1, V_FIRST_OCCUR + 1);
        ELSE
          V_SECOND_OCCUR := (INSTR(WORDS1, ',', V_FIRST_OCCUR + 1));
          V_PASS         := SUBSTR(WORDS1,
                                   V_FIRST_OCCUR + 1,
                                   V_SECOND_OCCUR - V_FIRST_OCCUR - 1);
        END IF;

      WHEN 2 THEN
        V_LENGTH      := LENGTH(WORDS2) - LENGTH(REPLACE(WORDS2, ','));
        V_RAND_NO2    := DBMS_RANDOM.VALUE(1, V_LENGTH);
        V_FIRST_OCCUR := (INSTR(WORDS2, ',', 1, V_RAND_NO2));
        IF (V_LENGTH = V_RAND_NO1) THEN
          V_PASS := SUBSTR(WORDS2, V_FIRST_OCCUR + 1);
        ELSE
          V_SECOND_OCCUR := (INSTR(WORDS2, ',', V_FIRST_OCCUR + 1));
          V_PASS         := SUBSTR(WORDS2,
                                   V_FIRST_OCCUR + 1,
                                   V_SECOND_OCCUR - V_FIRST_OCCUR - 1);
        END IF;

      WHEN 3 THEN
        V_LENGTH      := LENGTH(WORDS3) - LENGTH(REPLACE(WORDS3, ','));
        V_RAND_NO2    := DBMS_RANDOM.VALUE(1, V_LENGTH);
        V_FIRST_OCCUR := (INSTR(WORDS3, ',', 1, V_RAND_NO2));
        IF (V_LENGTH = V_RAND_NO1) THEN
          V_PASS := SUBSTR(WORDS3, V_FIRST_OCCUR + 1);
        ELSE
          V_SECOND_OCCUR := (INSTR(WORDS3, ',', V_FIRST_OCCUR + 1));
          V_PASS         := SUBSTR(WORDS3,
                                   V_FIRST_OCCUR + 1,
                                   V_SECOND_OCCUR - V_FIRST_OCCUR - 1);
        END IF;

      WHEN 4 THEN
        V_LENGTH      := LENGTH(WORDS4) - LENGTH(REPLACE(WORDS4, ','));
        V_RAND_NO2    := DBMS_RANDOM.VALUE(1, V_LENGTH);
        V_FIRST_OCCUR := (INSTR(WORDS4, ',', 1, V_RAND_NO2));
        IF (V_LENGTH = V_RAND_NO1) THEN
          V_PASS := SUBSTR(WORDS4, V_FIRST_OCCUR + 1);
        ELSE
          V_SECOND_OCCUR := (INSTR(WORDS4, ',', V_FIRST_OCCUR + 1));
          V_PASS         := SUBSTR(WORDS4,
                                   V_FIRST_OCCUR + 1,
                                   V_SECOND_OCCUR - V_FIRST_OCCUR - 1);
        END IF;

      WHEN 5 THEN
        V_LENGTH      := LENGTH(WORDS5) - LENGTH(REPLACE(WORDS5, ','));
        V_RAND_NO2    := DBMS_RANDOM.VALUE(1, V_LENGTH);
        V_FIRST_OCCUR := (INSTR(WORDS5, ',', 1, V_RAND_NO2));
        IF (V_LENGTH = V_RAND_NO1) THEN
          V_PASS := SUBSTR(WORDS5, V_FIRST_OCCUR + 1);
        ELSE
          V_SECOND_OCCUR := (INSTR(WORDS5, ',', V_FIRST_OCCUR + 1));
          V_PASS         := SUBSTR(WORDS5,
                                   V_FIRST_OCCUR + 1,
                                   V_SECOND_OCCUR - V_FIRST_OCCUR - 1);
        END IF;
    END CASE;
  ----------------------------------------------------------
    ------------- REMOVING WHITESPACE CHARACTER---------------
    ----------------------------------------------------------
    SELECT REGEXP_REPLACE(V_PASS, '\s*', '') INTO V_PASS FROM DUAL;

    ---------------------------------------------------------
    ----- CHECKING VULGAR OR NOT FOR PASSWORD ---------------
    ---------------------------------------------------------
    IF (ISACCESSCODE = 0) THEN
      IF ((INSTR(VULGAR, LOWER(V_PASS), 1)) > 0) THEN
        V_PASS := GENERATE_TAC(DESIREDLENGTH, ISACCESSCODE);
      ELSIF ((INSTR(NEGATIVE, LOWER(V_PASS), 1)) > 0) THEN
        V_PASS := GENERATE_TAC(DESIREDLENGTH, ISACCESSCODE);
      END IF;

    END IF;

    ---------------------------------------------------------
    ----- GENERATING ALFA NUMERIC VALUE ---------------------------
    ---------------------------------------------------------
    IF LENGTH(V_PASS)=0 THEN
       V_PASS := GENERATE_TAC(DESIREDLENGTH, ISACCESSCODE);
    END IF;
    
    V_LENGTH := DESIREDLENGTH - LENGTH(V_PASS);

    IF (V_LENGTH >= 1) THEN
      /*V_MAGNITUDE := SUBSTR(NINES, 1, V_LENGTH);
      VAL1        := ABS(MOD(DBMS_RANDOM.RANDOM, TO_NUMBER(V_MAGNITUDE)));*/
      VAL1   := GET_FIXED_LENGTH_RAND_NO(V_LENGTH);
      V_PASS := V_PASS || VAL1;
    ELSE
      V_PASS := SUBSTR(V_PASS,1,DESIREDLENGTH-1);
      VAL1   := ABS(MOD(DBMS_RANDOM.RANDOM, 9));
      V_PASS := V_PASS || VAL1;
    END IF;

    ---------------------------------------------------------
    ----- VALIDATING ACCESS CODE ---------------------------
    ---------------------------------------------------------

    IF (ISACCESSCODE = 1) THEN
      SELECT COUNT(ACCESS_CODE)
        INTO V_RECORD_COUNT
        FROM TEST_ADMIN
       WHERE upper(ACCESS_CODE) = upper(V_PASS);

      SELECT COUNT(TSIT.ACCESS_CODE)
        INTO V_RECORD_COUNT1
        FROM TEST_ADMIN_ITEM_SET TSIT
       WHERE upper(TSIT.ACCESS_CODE) = upper(V_PASS);

      IF (V_RECORD_COUNT > 0) OR (V_RECORD_COUNT1 > 0) THEN

        V_PASS := GENERATE_TAC(DESIREDLENGTH, ISACCESSCODE);
      END IF;
    END IF;

    RETURN UPPER(V_PASS);
  END GENERATE_TAC;

  -- for generating TestSessionStatus

  FUNCTION GETTESTSESSIONSTATUS(VCONFIGDATA TEST_SCHEDULE_CONFIG%ROWTYPE)
    RETURN VARCHAR2 IS
    V_STATUS TEST_ADMIN.TEST_ADMIN_STATUS%TYPE := 'FU';
  BEGIN
    IF ((TO_DATE(TO_CHAR(SYSDATE, 'YYYYMMDD'), 'YYYYMMDD') -
       (TO_DATE(TO_CHAR(VCONFIGDATA.LOGIN_END_DATE, 'YYYYMMDD'),
                  'YYYYMMDD')) > 0)) THEN
      V_STATUS := 'PA';
    ELSIF (((TO_DATE(TO_CHAR(SYSDATE, 'YYYYMMDD'), 'YYYYMMDD') -
          (TO_DATE(TO_CHAR(VCONFIGDATA.LOGIN_END_DATE, 'YYYYMMDD'),
                      'YYYYMMDD')) = 0)) AND
          (((TO_DATE(TO_CHAR(SYSDATE, 'HH24MISS'), 'HH24MISS') -
          (TO_DATE(TO_CHAR(VCONFIGDATA.DAILY_LOGIN_END_TIME, 'HH24MISS'),
                       'HH24MISS')) > 0)))) THEN
      V_STATUS := 'PA';
    ELSIF ((TO_DATE(TO_CHAR(SYSDATE, 'YYYYMMDD'), 'YYYYMMDD') -
          (TO_DATE(TO_CHAR(VCONFIGDATA.LOGIN_START_DATE, 'YYYYMMDD'),
                     'YYYYMMDD')) > 0)) THEN
      V_STATUS := 'CU';
    ELSIF (((TO_DATE(TO_CHAR(SYSDATE, 'YYYYMMDD'), 'YYYYMMDD') -
          (TO_DATE(TO_CHAR(VCONFIGDATA.LOGIN_START_DATE, 'YYYYMMDD'),
                      'YYYYMMDD')) = 0)) AND
          (((TO_DATE(TO_CHAR(SYSDATE, 'HH24MISS'), 'HH24MISS') -
          (TO_DATE(TO_CHAR(VCONFIGDATA.DAILY_LOGIN_START_TIME,
                               'HH24MISS'),
                       'HH24MISS')) > 0)))) THEN
      V_STATUS := 'CU';
    END IF;

    RETURN V_STATUS;
  END GETTESTSESSIONSTATUS;

  -- for getting PROGRAM_ID, also includes validation
  FUNCTION PROGRAMIDFORCUSTOMERNPRODUCT(CUSTOMERID NUMBER,
                                        PRODUCTID  NUMBER,
                                        STARTDATE  DATE) RETURN NUMBER AS
    V_PROGRAM_ID PROGRAM.PROGRAM_ID%TYPE := NULL;
  BEGIN
    SELECT MIN(PR.PROGRAM_ID)
      INTO V_PROGRAM_ID
      FROM PROGRAM PR, PRODUCT P
     WHERE PR.CUSTOMER_ID = CUSTOMERID
       AND PR.PRODUCT_ID = P.PARENT_PRODUCT_ID
       AND TRUNC(PR.PROGRAM_START_DATE) <= TRUNC(STARTDATE)
       AND TRUNC(PR.PROGRAM_END_DATE) >= TRUNC(STARTDATE)
       AND P.PRODUCT_ID = PRODUCTID;
    RETURN V_PROGRAM_ID;
  EXCEPTION
    WHEN TOO_MANY_ROWS THEN
      LOG_MESSAGE(V_LOG_ID,
                  V_STATUS_ERROR,
                  'Customer[' || CUSTOMERID ||
                  '] has too many programs for product [' || PRODUCTID || '].');
      RAISE_APPLICATION_ERROR(-20000,
                              'Customer[' || CUSTOMERID ||
                              '] has too many programs for product [' ||
                              PRODUCTID || '].');
    WHEN NO_DATA_FOUND THEN
      LOG_MESSAGE(V_LOG_ID,
                  V_STATUS_WARN,
                  'Customer does not have program product [' || PRODUCTID || '].');
      RETURN NULL;
  END PROGRAMIDFORCUSTOMERNPRODUCT;

  -- validation for Restricted Student 1 for non restricted 0 restricted
  FUNCTION ISTESTRESTRICTEDFORSTUDENT(STUDENTID     NUMBER,
                                      TESTITEMSETID NUMBER) RETURN NUMBER AS
    V_IS_TEST_RESTRICTED NUMBER(2) := 0;
  BEGIN
    SELECT DECODE(COUNT(*), 0, 1, 0)
      INTO V_IS_TEST_RESTRICTED
      FROM TEST_ADMIN ADM, TEST_ROSTER ROS
     WHERE ROS.STUDENT_ID = STUDENTID
       AND ROS.TEST_ADMIN_ID = ADM.TEST_ADMIN_ID
       AND ADM.ITEM_SET_ID = TESTITEMSETID;

    RETURN V_IS_TEST_RESTRICTED;

  END ISTESTRESTRICTEDFORSTUDENT;

  --------------------------------------------
  ----------- Procedure for logging ----------
  --------------------------------------------

  PROCEDURE LOG_MESSAGE(P_ID            INTEGER,
                        P_STATUS        IN VARCHAR2,
                        P_ERROR_MESSAGE IN VARCHAR2) AS
    PRAGMA AUTONOMOUS_TRANSACTION;
  BEGIN
    INSERT INTO OAS_TEST_SCHEDULE_LOG
      (ID, LOG_TIME, STATUS, LOG_MESSAGE)
    VALUES
      (P_ID, SYSTIMESTAMP, P_STATUS, SUBSTR(P_ERROR_MESSAGE, 1, 4000));
    COMMIT;
  END;

  --------------------------------------------
  ----------- Procedure for         ----------
  --------------------------------------------

  PROCEDURE INSERT_TEST_ADMIN(VPRODUCTID           INTEGER,
                              VCUSTOMERID          INTEGER,
                              RECTCITEMSETID       CRSGETTCTESTFORPRODUCT%ROWTYPE,
                              VCONFIGDATA          TEST_SCHEDULE_CONFIG%ROWTYPE,
                              VSHOWSTUDENTFEEDBACK PRODUCT.SHOW_STUDENT_FEEDBACK%TYPE,
                              VTESTCATALOGID       TEST_CATALOG.TEST_CATALOG_ID %TYPE,
                              VCREATEDBY           USERS.USER_ID%TYPE,
                              VTESTAMINACCESSCODE  TEST_ADMIN_ITEM_SET.ACCESS_CODE%TYPE,
                              ORG_NODE_ID          ORG_NODE.ORG_NODE_ID%TYPE,
                              VTESTADMINID         TEST_ADMIN.TEST_ADMIN_ID%TYPE) AS

    -------------------------------------------------------------
    --- generating TEST SESSION STATUS

    V_TEST_SESSION_STATUS VARCHAR2(10) := GETTESTSESSIONSTATUS(VCONFIGDATA);
    V_SESSION_SEQUENCE    NUMBER(4);

  BEGIN

    SELECT COUNT(1) + 1
      INTO V_SESSION_SEQUENCE
      FROM TEST_ADMIN TA
     WHERE TA.CREATOR_ORG_NODE_ID = ORG_NODE_ID
       AND TA.PRODUCT_ID = VPRODUCTID;
       --AND TA.ITEM_SET_ID = RECTCITEMSETID.ITEM_SET_ID;

    INSERT INTO TEST_ADMIN
      (TEST_ADMIN_ID,
       CUSTOMER_ID,
       TEST_ADMIN_NAME,
       PRODUCT_ID,
       CREATOR_ORG_NODE_ID,
       ACCESS_CODE,
       LOCATION,
       LOGIN_START_DATE,
       LOGIN_END_DATE,
       DAILY_LOGIN_START_TIME,
       DAILY_LOGIN_END_TIME,
       CREATED_BY,
       ACTIVATION_STATUS,
       ITEM_SET_ID,
       TEST_ADMIN_STATUS,
       SESSION_NUMBER,
       TEST_ADMIN_TYPE,
       ENFORCE_TIME_LIMIT,
       ENFORCE_BREAK,
       TIME_ZONE,
       TEST_CATALOG_ID,
       PREFERRED_FORM,
       FORM_ASSIGNMENT_METHOD,
       SHOW_STUDENT_FEEDBACK,
       PROGRAM_ID,
       RANDOM_DISTRACTOR_STATUS,
       CREATED_DATE_TIME)
    VALUES
      (VTESTADMINID,
       VCUSTOMERID,
       RECTCITEMSETID.ITEM_SET_DISPLAY_NAME || ' - ' || V_SESSION_SEQUENCE,
       VPRODUCTID,
       ORG_NODE_ID,
       VTESTAMINACCESSCODE,
       VCONFIGDATA.LOCATION,
       VCONFIGDATA.LOGIN_START_DATE,
       VCONFIGDATA.LOGIN_END_DATE,
       VCONFIGDATA.DAILY_LOGIN_START_TIME,
       VCONFIGDATA.DAILY_LOGIN_END_TIME,
       VCREATEDBY,
       V_ACTIVATION_STATUS,
       RECTCITEMSETID.ITEM_SET_ID,
       V_TEST_SESSION_STATUS,
       (ABS(MOD(DBMS_RANDOM.RANDOM, 9999999))),
       TEST_ADMIN_TYPE,
       ENFORCE_TIME_LIMIT,
       ENFORCE_BREAK,
       VCONFIGDATA.TIME_ZONE,
       VTESTCATALOGID,
       PREFERRED_FORM,
       FORM_ASSIGNMENT_METHOD,
       VSHOWSTUDENTFEEDBACK,
       PROGRAMIDFORCUSTOMERNPRODUCT(VCUSTOMERID,
                                    VPRODUCTID,
                                    VCONFIGDATA.LOGIN_START_DATE),
       RECTCITEMSETID.RANDOM_DISTRACTOR_ALLOWABLE,
       SYSDATE);

    /*
    Insert the user recored associated with created test session
    */

    INSERT INTO TEST_ADMIN_USER_ROLE
      (TEST_ADMIN_ID, USER_ID, ROLE_ID, CREATED_BY, CREATED_DATE_TIME)
    VALUES
      (VTESTADMINID, VCREATEDBY, ACC_MANAGER_ROLE_ID, VCREATEDBY, SYSDATE);

  END INSERT_TEST_ADMIN;

  --------------------------------------------
  ----------- Procedure for         ----------
  --------------------------------------------
  PROCEDURE INSERT_ALL_STU_AT_SCHOOL(VPRODUCTID          INTEGER,
                                     VCUSTOMERID         INTEGER,
                                     VCREATEDBY          USERS.USER_ID%TYPE,
                                     VTESTADMINID        TEST_ADMIN.TEST_ADMIN_ID%TYPE,
                                     PGRADE              ITEM_SET.GRADE %TYPE,
                                     ORG_NODE_ID         ORG_NODE.ORG_NODE_ID%TYPE,
                                     VSTUDENTLIST        IN OUT VARCHAR2,
                                     VCUSTOMERFLAGSTATUS CUSTOMER_CONFIGURATION.DEFAULT_VALUE%TYPE,
                                     INTESTCATALOGID INTEGER) AS

    V_IS_CURSOR_RETURNS_VALUE BOOLEAN := FALSE;
    V_STU_PASSWORD            VARCHAR2(32);
  BEGIN

    OPEN CRSGETSTUFORALLGRADEATSCHOOL(PGRADE, ORG_NODE_ID, VPRODUCTID, INTESTCATALOGID);
    LOOP
      FETCH CRSGETSTUFORALLGRADEATSCHOOL BULK COLLECT
        INTO V_ARRAY_OF_STUDENT LIMIT 500;

      EXIT WHEN(V_ARRAY_OF_STUDENT.COUNT = 0);

      FOR I IN 1 .. V_ARRAY_OF_STUDENT.COUNT LOOP

        --- ACTUAL CODE START HERE
        V_IS_CURSOR_RETURNS_VALUE := TRUE;

        IF ((VSTUDENTLIST IS NOT NULL) AND (LENGTH(TRIM(VSTUDENTLIST)) > 0)) THEN
          VSTUDENTLIST := VSTUDENTLIST || ',' || V_ARRAY_OF_STUDENT(I)
                         .STUDENT_ID;
        ELSE
          VSTUDENTLIST := V_ARRAY_OF_STUDENT(I).STUDENT_ID;
        END IF;

        /*  DBMS_OUTPUT.put_line('Admin'||V_RESTRICTED_ADMIN);*/
        BEGIN
          V_STU_PASSWORD := GENERATEPASSWORD(V_STD_PASS_MIN_LENGTH,
                                             V_STD_PASS_MAX_LENGTH,
                                             0);
          INSERT INTO TEST_ROSTER
            (TEST_ROSTER_ID,
             TEST_ADMIN_ID,
             TEST_COMPLETION_STATUS,
             VALIDATION_STATUS,
             OVERRIDE_TEST_WINDOW,
             PASSWORD,
             STUDENT_ID,
             CREATED_BY,
             ACTIVATION_STATUS,
             CUSTOMER_ID,
             SCORING_STATUS,
             ORG_NODE_ID,
             FORM_ASSIGNMENT,
             CUSTOMER_FLAG_STATUS)
          VALUES
            (SEQ_TEST_ROSTER_ID.NEXTVAL,
             VTESTADMINID,
             TEST_COMPLETION_STATUS,
             VALIDATION_STATUS,
             OVERRIDE_TEST_WINDOW,
             V_STU_PASSWORD,
             V_ARRAY_OF_STUDENT(I).STUDENT_ID,
             VCREATEDBY,
             V_ACTIVATION_STATUS,
             VCUSTOMERID,
             SCORING_STATUS,
             V_ARRAY_OF_STUDENT(I).ORG_NODE_ID,
             V_FORM_ASSIGNMENT,
             VCUSTOMERFLAGSTATUS);

        EXCEPTION
          WHEN OTHERS THEN
            LOG_MESSAGE(V_LOG_ID,
                        V_STATUS_ERROR,
                        'Insert into table test_roster fails' ||
                        DBMS_UTILITY.FORMAT_ERROR_BACKTRACE);
            RAISE;
        END;

      --- ACTUAL CODE END HERE

      END LOOP;
      EXIT WHEN CRSGETSTUFORALLGRADEATSCHOOL%NOTFOUND;
    END LOOP;

    CLOSE CRSGETSTUFORALLGRADEATSCHOOL;

    IF (NOT V_IS_CURSOR_RETURNS_VALUE) THEN
      -- LOGGING ONLY, WHEN CURSOR "crsGetStuForAllGradeAtSchool" DOES NOT RETURNS ANY VALUE
      LOG_MESSAGE(V_LOG_ID,
                  V_STATUS_WARN,
                  'No student found for school id [' || ORG_NODE_ID ||
                  '] with grade [' || PGRADE || '].');
      -- RAISE_APPLICATION_ERROR(-20000,'No student found for school id ['||recSchoolNode.org_node_id || '] with grade ['||recTCItemSetId.grade||'].');
    END IF;

  END INSERT_ALL_STU_AT_SCHOOL;

  PROCEDURE INSERT_ALL_STU_AT_SCHOOL_RR(VPRODUCTID          INTEGER,
                                        VCUSTOMERID         INTEGER,
                                        VCREATEDBY          USERS.USER_ID%TYPE,
                                        VTESTADMINID        TEST_ADMIN.TEST_ADMIN_ID%TYPE,
                                        PGRADE              ITEM_SET.GRADE %TYPE,
                                        ORG_NODE_ID         ORG_NODE.ORG_NODE_ID%TYPE,
                                        VSTUDENTLIST        IN OUT VARCHAR2,
                                        VCUSTOMERFLAGSTATUS CUSTOMER_CONFIGURATION.DEFAULT_VALUE%TYPE,
                                        INTESTCATALOGID INTEGER) AS

    V_IS_CURSOR_RETURNS_VALUE BOOLEAN := FALSE;
    V_FORM_NAME               VARCHAR2(10);
    FORM_COUNT                INTEGER := 1;
    ROUND_ROBIN_COUNT         INTEGER := 1;
    V_STU_PASSWORD            VARCHAR2(32);
  BEGIN

    ----Round robin form assignment implementation
    FOR RECGETFORMSASSIGNMENT IN CRSGETFORMSASSIGNMENT(VTESTADMINID) LOOP
      V_ARRAY_FORM(FORM_COUNT) := RECGETFORMSASSIGNMENT.FORM;
      FORM_COUNT := FORM_COUNT + 1;
    END LOOP;

    OPEN CRSGETSTUFORALLGRADEATSCHOOL(PGRADE, ORG_NODE_ID, VPRODUCTID, INTESTCATALOGID);
    LOOP
      FETCH CRSGETSTUFORALLGRADEATSCHOOL BULK COLLECT
        INTO V_ARRAY_OF_STUDENT LIMIT 500;

      EXIT WHEN(V_ARRAY_OF_STUDENT.COUNT = 0);

      FOR I IN 1 .. V_ARRAY_OF_STUDENT.COUNT LOOP

        --- ACTUAL CODE START HERE
        V_IS_CURSOR_RETURNS_VALUE := TRUE;

        IF ((VSTUDENTLIST IS NOT NULL) AND (LENGTH(TRIM(VSTUDENTLIST)) > 0)) THEN
          VSTUDENTLIST := VSTUDENTLIST || ',' || V_ARRAY_OF_STUDENT(I)
                         .STUDENT_ID;
        ELSE
          VSTUDENTLIST := V_ARRAY_OF_STUDENT(I).STUDENT_ID;
        END IF;

        ----Round robin form assignment implementation
        V_FORM_NAME := V_ARRAY_FORM(ROUND_ROBIN_COUNT);

        /*  DBMS_OUTPUT.put_line('Admin'||V_RESTRICTED_ADMIN);*/
        BEGIN
          V_STU_PASSWORD := GENERATEPASSWORD(V_STD_PASS_MIN_LENGTH,
                                             V_STD_PASS_MAX_LENGTH,
                                             0);
          INSERT INTO TEST_ROSTER
            (TEST_ROSTER_ID,
             TEST_ADMIN_ID,
             TEST_COMPLETION_STATUS,
             VALIDATION_STATUS,
             OVERRIDE_TEST_WINDOW,
             PASSWORD,
             STUDENT_ID,
             CREATED_BY,
             ACTIVATION_STATUS,
             CUSTOMER_ID,
             SCORING_STATUS,
             ORG_NODE_ID,
             FORM_ASSIGNMENT,
             CUSTOMER_FLAG_STATUS)
          VALUES
            (SEQ_TEST_ROSTER_ID.NEXTVAL,
             VTESTADMINID,
             TEST_COMPLETION_STATUS,
             VALIDATION_STATUS,
             OVERRIDE_TEST_WINDOW,
             V_STU_PASSWORD,
             V_ARRAY_OF_STUDENT(I).STUDENT_ID,
             VCREATEDBY,
             V_ACTIVATION_STATUS,
             VCUSTOMERID,
             SCORING_STATUS,
             V_ARRAY_OF_STUDENT(I).ORG_NODE_ID,
             V_FORM_NAME,
             VCUSTOMERFLAGSTATUS);

        EXCEPTION
          WHEN OTHERS THEN
            LOG_MESSAGE(V_LOG_ID,
                        V_STATUS_ERROR,
                        'Insert into table test_roster fails' ||
                        DBMS_UTILITY.FORMAT_ERROR_BACKTRACE);
            RAISE;
        END;

        ----Round robin form assignment implementation
        ROUND_ROBIN_COUNT := ROUND_ROBIN_COUNT + 1;
        IF ROUND_ROBIN_COUNT = FORM_COUNT THEN
          ROUND_ROBIN_COUNT := 1;
        END IF;
        --- ACTUAL CODE END HERE

      END LOOP;
      EXIT WHEN CRSGETSTUFORALLGRADEATSCHOOL%NOTFOUND;
    END LOOP;

    CLOSE CRSGETSTUFORALLGRADEATSCHOOL;

    IF (NOT V_IS_CURSOR_RETURNS_VALUE) THEN
      -- LOGGING ONLY, WHEN CURSOR "crsGetStuForAllGradeAtSchool" DOES NOT RETURNS ANY VALUE
      LOG_MESSAGE(V_LOG_ID,
                  V_STATUS_WARN,
                  'No student found for school id [' || ORG_NODE_ID ||
                  '] with grade [' || PGRADE || '].');
      -- RAISE_APPLICATION_ERROR(-20000,'No student found for school id ['||recSchoolNode.org_node_id || '] with grade ['||recTCItemSetId.grade||'].');
    END IF;

  END INSERT_ALL_STU_AT_SCHOOL_RR;

  PROCEDURE INSERT_ALL_STU_AT_SCHOOL_FIXED(VPRODUCTID          INTEGER,
                                           VCUSTOMERID         INTEGER,
                                           VFORM               VARCHAR2,
                                           VCREATEDBY          USERS.USER_ID%TYPE,
                                           VTESTADMINID        TEST_ADMIN.TEST_ADMIN_ID%TYPE,
                                           PGRADE              ITEM_SET.GRADE %TYPE,
                                           ORG_NODE_ID         ORG_NODE.ORG_NODE_ID%TYPE,
                                           VSTUDENTLIST        IN OUT VARCHAR2,
                                           VCUSTOMERFLAGSTATUS CUSTOMER_CONFIGURATION.DEFAULT_VALUE%TYPE,
                                           INTESTCATALOGID INTEGER) AS

    V_IS_CURSOR_RETURNS_VALUE BOOLEAN := FALSE;
    V_STU_PASSWORD            VARCHAR2(32);
  BEGIN

    OPEN CRSGETSTUFORALLGRADEATSCHOOL(PGRADE, ORG_NODE_ID, VPRODUCTID, INTESTCATALOGID);
    LOOP
      FETCH CRSGETSTUFORALLGRADEATSCHOOL BULK COLLECT
        INTO V_ARRAY_OF_STUDENT LIMIT 500;

      EXIT WHEN(V_ARRAY_OF_STUDENT.COUNT = 0);

      FOR I IN 1 .. V_ARRAY_OF_STUDENT.COUNT LOOP

        --- ACTUAL CODE START HERE
        V_IS_CURSOR_RETURNS_VALUE := TRUE;

        IF ((VSTUDENTLIST IS NOT NULL) AND (LENGTH(TRIM(VSTUDENTLIST)) > 0)) THEN
          VSTUDENTLIST := VSTUDENTLIST || ',' || V_ARRAY_OF_STUDENT(I)
                         .STUDENT_ID;
        ELSE
          VSTUDENTLIST := V_ARRAY_OF_STUDENT(I).STUDENT_ID;
        END IF;

        /*  DBMS_OUTPUT.put_line('Admin'||V_RESTRICTED_ADMIN);*/
        BEGIN
          V_STU_PASSWORD := GENERATEPASSWORD(V_STD_PASS_MIN_LENGTH,
                                             V_STD_PASS_MAX_LENGTH,
                                             0);
          INSERT INTO TEST_ROSTER
            (TEST_ROSTER_ID,
             TEST_ADMIN_ID,
             TEST_COMPLETION_STATUS,
             VALIDATION_STATUS,
             OVERRIDE_TEST_WINDOW,
             PASSWORD,
             STUDENT_ID,
             CREATED_BY,
             ACTIVATION_STATUS,
             CUSTOMER_ID,
             SCORING_STATUS,
             ORG_NODE_ID,
             FORM_ASSIGNMENT,
             CUSTOMER_FLAG_STATUS)
          VALUES
            (SEQ_TEST_ROSTER_ID.NEXTVAL,
             VTESTADMINID,
             TEST_COMPLETION_STATUS,
             VALIDATION_STATUS,
             OVERRIDE_TEST_WINDOW,
             V_STU_PASSWORD,
             V_ARRAY_OF_STUDENT(I).STUDENT_ID,
             VCREATEDBY,
             V_ACTIVATION_STATUS,
             VCUSTOMERID,
             SCORING_STATUS,
             V_ARRAY_OF_STUDENT(I).ORG_NODE_ID,
             VFORM,
             VCUSTOMERFLAGSTATUS);

        EXCEPTION
          WHEN OTHERS THEN
            LOG_MESSAGE(V_LOG_ID,
                        V_STATUS_ERROR,
                        'Insert into table test_roster fails' ||
                        DBMS_UTILITY.FORMAT_ERROR_BACKTRACE);
            RAISE;
        END;
        --- ACTUAL CODE END HERE

      END LOOP;
      EXIT WHEN CRSGETSTUFORALLGRADEATSCHOOL%NOTFOUND;
    END LOOP;

    CLOSE CRSGETSTUFORALLGRADEATSCHOOL;

    IF (NOT V_IS_CURSOR_RETURNS_VALUE) THEN
      -- LOGGING ONLY, WHEN CURSOR "crsGetStuForAllGradeAtSchool" DOES NOT RETURNS ANY VALUE
      LOG_MESSAGE(V_LOG_ID,
                  V_STATUS_WARN,
                  'No student found for school id [' || ORG_NODE_ID ||
                  '] with grade [' || PGRADE || '].');
      -- RAISE_APPLICATION_ERROR(-20000,'No student found for school id ['||recSchoolNode.org_node_id || '] with grade ['||recTCItemSetId.grade||'].');
    END IF;

  END INSERT_ALL_STU_AT_SCHOOL_FIXED;

  --------------------------------------------
  ----------- Procedure for         ----------
  --------------------------------------------

  PROCEDURE INSERT_SCHEDULABLE_UNITS(VCUSTOMERID    INTEGER,
                                     RECTCITEMSETID CRSGETTCTESTFORPRODUCT%ROWTYPE,
                                     NODE_NAME      ORG_NODE.ORG_NODE_NAME%TYPE,
                                     VTESTADMINID   TEST_ADMIN.TEST_ADMIN_ID%TYPE,
                                     PACCESSCODE    TEST_ADMIN_ITEM_SET.ACCESS_CODE%TYPE,
                                     VSTUDENTLIST   VARCHAR2) AS
    V_IS_CURSOR_RETURNS_VALUE BOOLEAN := FALSE;
    VITEMSETORDER             INTEGER := 0;
    VACCESSCODE               TEST_ADMIN_ITEM_SET.ACCESS_CODE%TYPE := PACCESSCODE;
    VTDITEMSETORDER           TEST_ADMIN_ITEM_SET.ITEM_SET_ORDER%TYPE := 0;
    /* In clause limit fix : Start */
    V_STU_COUNT         INTEGER := 0;
    V_LOOP_COUTER       INTEGER := 0;
    V_TEMP_STU_LIST     VARCHAR2(32767) := '';
    V_ELEMENT_OCCURENCE INTEGER := 0;
    V_START_POS         INTEGER := 0;
    V_PREV_INDEX        INTEGER := 0;
    V_STR_INDEX         INTEGER := 0;
    V_SUBSTR_LENGTH     INTEGER := 0;
    /* In clause limit fix : End */
  BEGIN

    FOR RECTSITEMSETID IN CRSGETSCHEDULABLEUNITSFORTEST(RECTCITEMSETID.ITEM_SET_ID) LOOP

      V_IS_CURSOR_RETURNS_VALUE := TRUE;

      INSERT INTO TEST_ADMIN_ITEM_SET
        (ITEM_SET_ID, TEST_ADMIN_ID, ITEM_SET_ORDER, ACCESS_CODE)
      VALUES
        (RECTSITEMSETID.ITEM_SET_ID,
         VTESTADMINID,
         VITEMSETORDER,
         VACCESSCODE);

      VITEMSETORDER := VITEMSETORDER + 1;
      VACCESSCODE   := GENERATE_TAC(V_GENERATED_ACCESS_CODE_LENGTH, 1);

      IF ((VSTUDENTLIST IS NOT NULL) AND (LENGTH(TRIM(VSTUDENTLIST)) > 0)) THEN

        ---------- FOR LOOP 1.1.2.1  ------

        V_IS_CURSOR_RETURNS_VALUE := FALSE;
        FOR RECTDITEMSETID IN CRSGETTDTESTELEMENTLIST(RECTSITEMSETID.ITEM_SET_ID,
                                                      V_FORM_ASSIGNMENT) LOOP
          V_IS_CURSOR_RETURNS_VALUE := TRUE;
          /** IN clause limit fix : START **/
          -- Reset all the counters for each TD level
          V_STU_COUNT         := 0;
          V_LOOP_COUTER       := 0;
          V_TEMP_STU_LIST     := '';
          V_ELEMENT_OCCURENCE := 0;
          V_START_POS         := 0;
          V_PREV_INDEX        := 0;
          V_STR_INDEX         := 0;
          -- dbms_output.put_line('check1');

          V_STU_COUNT := (LENGTH(VSTUDENTLIST) -
                         LENGTH(REPLACE(VSTUDENTLIST, ',')) + 1);
          -- dbms_output.put_line('check2');

          IF V_STU_COUNT > 999 THEN

            V_LOOP_COUTER := FLOOR(V_STU_COUNT / 999);

            IF MOD(V_STU_COUNT, 999) > 0 THEN
              V_LOOP_COUTER := V_LOOP_COUTER + 1;
            END IF;
            --dbms_output.put_line(V_LOOP_COUTER);
            FOR J IN 1 .. V_LOOP_COUTER LOOP
              V_ELEMENT_OCCURENCE := J * 999;
              --dbms_output.put_line(J);

              V_STR_INDEX := INSTR(VSTUDENTLIST,
                                   ',',
                                   1,
                                   V_ELEMENT_OCCURENCE);
              IF V_STR_INDEX = 0 THEN
                V_SUBSTR_LENGTH := LENGTH(VSTUDENTLIST);
              ELSE

                V_SUBSTR_LENGTH := (V_STR_INDEX - 1) - V_PREV_INDEX;
              END IF;

              V_TEMP_STU_LIST := SUBSTR(VSTUDENTLIST,
                                        V_START_POS,
                                        V_SUBSTR_LENGTH);
              -- Need to insert into siss table here using temp student list
              EXECUTE IMMEDIATE 'insert into student_item_set_status
                (test_roster_id,
                 item_set_id,
                 completion_status,
                 validation_status,
                 time_expired,
                 item_set_order,
                 customer_flag_status)
                (select distinct test_roster_id,' ||
                                RECTDITEMSETID.ITEM_SET_ID || ',' || '''' ||
                                TEST_COMPLETION_STATUS || '''' || ',' || '''' ||
                                VALIDATION_STATUS || '''' || ',' || '''' ||
                                TEST_TIME_EXPIRED || '''' || ',' ||
                                VTDITEMSETORDER || ',
                                 max(default_value) as default_value
                   from (select distinct test_roster_id, cc.default_value
                           from test_roster, customer_configuration cc
                          where cc.customer_id = ' ||
                                VCUSTOMERID || '
                            and cc.customer_configuration_name = ' || '''' ||
                                VROSTER_STATUS_FLAG || '''' ||
                                ' and test_admin_id = ' || VTESTADMINID || '
                            and student_id in (' ||
                                V_TEMP_STU_LIST || ' )
                         union
                         select distinct test_roster_id, null as default_value
                           from test_roster
                          where test_admin_id = ' ||
                                VTESTADMINID || '
                            and student_id in (' ||
                                V_TEMP_STU_LIST || '))
                  group by test_roster_id)';

              V_PREV_INDEX    := V_STR_INDEX;
              V_START_POS     := V_PREV_INDEX + 1;
              V_TEMP_STU_LIST := '';
            END LOOP;

          ELSE
            /*dbms_output.put_line(V_STU_COUNT ||
            '..old logic will work...');*/
            EXECUTE IMMEDIATE 'insert into student_item_set_status
                (test_roster_id,
                 item_set_id,
                 completion_status,
                 validation_status,
                 time_expired,
                 item_set_order,
                 customer_flag_status)
                (select distinct test_roster_id,' ||
                              RECTDITEMSETID.ITEM_SET_ID || ',' || '''' ||
                              TEST_COMPLETION_STATUS || '''' || ',' || '''' ||
                              VALIDATION_STATUS || '''' || ',' || '''' ||
                              TEST_TIME_EXPIRED || '''' || ',' ||
                              VTDITEMSETORDER || ',
                                 max(default_value) as default_value
                   from (select distinct test_roster_id, cc.default_value
                           from test_roster, customer_configuration cc
                          where cc.customer_id = ' ||
                              VCUSTOMERID || '
                            and cc.customer_configuration_name = ' || '''' ||
                              VROSTER_STATUS_FLAG || '''' ||
                              ' and test_admin_id = ' || VTESTADMINID || '
                            and student_id in (' ||
                              VSTUDENTLIST || ' )
                         union
                         select distinct test_roster_id, null as default_value
                           from test_roster
                          where test_admin_id = ' ||
                              VTESTADMINID || '
                            and student_id in (' ||
                              VSTUDENTLIST || '))
                  group by test_roster_id)';

          END IF;
          /** IN clause limit fix : END **/

          LOG_MESSAGE(V_LOG_ID,
                      V_STATUS_SUCCESS,
                      'For Grade:' || RECTCITEMSETID.GRADE ||
                      ', For School : ' || NODE_NAME /*RECSCHOOLNODE.ORG_NODE_NAME*/
                      || ', Test Assignment is Successful for  This test :' ||
                      RECTCITEMSETID.ITEM_SET_DISPLAY_NAME ||
                      ', no Of Students are Scheduled :' ||
                      (LENGTH(VSTUDENTLIST) -
                      LENGTH(REPLACE(VSTUDENTLIST, ',')) + 1));

          VTDITEMSETORDER := VTDITEMSETORDER + 1;
        END LOOP;

        IF (NOT V_IS_CURSOR_RETURNS_VALUE) THEN
          -- LOGGING ONLY, WHEN CURSOR "crsGetTDTestElementList" DOES NOT RETURNS ANY VALUE
          LOG_MESSAGE(V_LOG_ID,
                      V_STATUS_WARN,
                      'No valid Item found for itemset id [' ||
                      RECTSITEMSETID.ITEM_SET_ID || '] with type TD.');
          V_IS_CURSOR_RETURNS_VALUE := TRUE; -- SETTING TRUE AS PREVIOUS LOOP IS EXECUTING
        END IF;

      ELSE
        LOG_MESSAGE(V_LOG_ID,
                    V_STATUS_WARN,
                    'No Test Assignment is Successful for  This test :' ||
                    RECTCITEMSETID.ITEM_SET_DISPLAY_NAME ||
                    ', Reason no  Students are there in the school or Already the students are scheduled ');
      END IF;
    END LOOP;

    IF (NOT V_IS_CURSOR_RETURNS_VALUE) THEN
      -- LOGGING ONLY, WHEN CURSOR "crsGetSchedulableUnitsForTest" DOES NOT RETURNS ANY VALUE
      LOG_MESSAGE(V_LOG_ID,
                  V_STATUS_WARN,
                  'No valid Item found for itemset id [' ||
                  RECTCITEMSETID.ITEM_SET_ID || '] with type TS.');
      -- IF YOU DO NOT WISH THROW ERROR THEN SET TO  TRUE "V_IS_CURSOR_RETURNS_VALUE" AS PREVIOUS LOOP IS EXECUTING
      RAISE_APPLICATION_ERROR(-20000,
                              'No valid Item found for itemset id [' ||
                              RECTCITEMSETID.ITEM_SET_ID ||
                              '] with type TS.');
    END IF;

  END INSERT_SCHEDULABLE_UNITS;

  PROCEDURE INSERT_SCHEDULABLE_UNITS_RR(VCUSTOMERID    INTEGER,
                                        RECTCITEMSETID CRSGETTCTESTFORPRODUCT%ROWTYPE,
                                        NODE_NAME      ORG_NODE.ORG_NODE_NAME%TYPE,
                                        VTESTADMINID   TEST_ADMIN.TEST_ADMIN_ID%TYPE,
                                        PACCESSCODE    TEST_ADMIN_ITEM_SET.ACCESS_CODE%TYPE,
                                        VSTUDENTLIST   VARCHAR2) AS
    V_IS_CURSOR_RETURNS_VALUE BOOLEAN := FALSE;
    VITEMSETORDER             INTEGER := 0;
    VACCESSCODE               TEST_ADMIN_ITEM_SET.ACCESS_CODE%TYPE := PACCESSCODE;
    VTDITEMSETORDER           TEST_ADMIN_ITEM_SET.ITEM_SET_ORDER%TYPE := 0;
    VTDITEMSETORDERFORM       TEST_ADMIN_ITEM_SET.ITEM_SET_ORDER%TYPE := 0;
    /* In clause limit fix : Start */
    V_STU_COUNT         INTEGER := 0;
    V_LOOP_COUTER       INTEGER := 0;
    V_TEMP_STU_LIST     VARCHAR2(32767) := '';
    V_ELEMENT_OCCURENCE INTEGER := 0;
    V_START_POS         INTEGER := 0;
    V_PREV_INDEX        INTEGER := 0;
    V_STR_INDEX         INTEGER := 0;
    V_SUBSTR_LENGTH     INTEGER := 0;
    /* In clause limit fix : End */
  BEGIN

    FOR RECTSITEMSETID IN CRSGETSCHEDULABLEUNITSFORTEST(RECTCITEMSETID.ITEM_SET_ID) LOOP

      V_IS_CURSOR_RETURNS_VALUE := TRUE;

      INSERT INTO TEST_ADMIN_ITEM_SET
        (ITEM_SET_ID, TEST_ADMIN_ID, ITEM_SET_ORDER, ACCESS_CODE)
      VALUES
        (RECTSITEMSETID.ITEM_SET_ID,
         VTESTADMINID,
         VITEMSETORDER,
         VACCESSCODE);

      VITEMSETORDER := VITEMSETORDER + 1;
      VACCESSCODE   := GENERATE_TAC(V_GENERATED_ACCESS_CODE_LENGTH, 1);

      IF ((VSTUDENTLIST IS NOT NULL) AND (LENGTH(TRIM(VSTUDENTLIST)) > 0)) THEN

        ---------- FOR LOOP 1.1.2.1  ------

        V_IS_CURSOR_RETURNS_VALUE := FALSE;
        FOR I IN 1 .. V_ARRAY_FORM.COUNT LOOP
          VTDITEMSETORDERFORM := VTDITEMSETORDER;
          FOR RECTDITEMSETID IN CRSGETTDTESTELEMENTLIST(RECTSITEMSETID.ITEM_SET_ID,
                                                        V_ARRAY_FORM(I)) LOOP
            V_IS_CURSOR_RETURNS_VALUE := TRUE;
            /** IN clause limit fix : START **/
            -- Reset all the counters for each TD level
            V_STU_COUNT         := 0;
            V_LOOP_COUTER       := 0;
            V_TEMP_STU_LIST     := '';
            V_ELEMENT_OCCURENCE := 0;
            V_START_POS         := 0;
            V_PREV_INDEX        := 0;
            V_STR_INDEX         := 0;
            -- dbms_output.put_line('check1');

            V_STU_COUNT := (LENGTH(VSTUDENTLIST) -
                           LENGTH(REPLACE(VSTUDENTLIST, ',')) + 1);
            -- dbms_output.put_line('check2');

            IF V_STU_COUNT > 999 THEN

              V_LOOP_COUTER := FLOOR(V_STU_COUNT / 999);

              IF MOD(V_STU_COUNT, 999) > 0 THEN
                V_LOOP_COUTER := V_LOOP_COUTER + 1;
              END IF;
              --dbms_output.put_line(V_LOOP_COUTER);
              FOR J IN 1 .. V_LOOP_COUTER LOOP
                V_ELEMENT_OCCURENCE := J * 999;
                --dbms_output.put_line(J);

                V_STR_INDEX := INSTR(VSTUDENTLIST,
                                     ',',
                                     1,
                                     V_ELEMENT_OCCURENCE);
                IF V_STR_INDEX = 0 THEN
                  V_SUBSTR_LENGTH := LENGTH(VSTUDENTLIST);
                ELSE

                  V_SUBSTR_LENGTH := (V_STR_INDEX - 1) - V_PREV_INDEX;
                END IF;

                V_TEMP_STU_LIST := SUBSTR(VSTUDENTLIST,
                                          V_START_POS,
                                          V_SUBSTR_LENGTH);
                -- Need to insert into siss table here using temp student list
                EXECUTE IMMEDIATE 'insert into student_item_set_status
                  (test_roster_id,
                   item_set_id,
                   completion_status,
                   validation_status,
                   time_expired,
                   item_set_order,
                   customer_flag_status)
                  (select distinct test_roster_id,' ||
                                  RECTDITEMSETID.ITEM_SET_ID || ',' || '''' ||
                                  TEST_COMPLETION_STATUS || '''' || ',' || '''' ||
                                  VALIDATION_STATUS || '''' || ',' || '''' ||
                                  TEST_TIME_EXPIRED || '''' || ',' ||
                                  VTDITEMSETORDERFORM || ',
                                   max(default_value) as default_value
                     from (select distinct test_roster_id, cc.default_value
                             from test_roster, customer_configuration cc
                            where cc.customer_id = ' ||
                                  VCUSTOMERID || '
                              and cc.customer_configuration_name = ' || '''' ||
                                  VROSTER_STATUS_FLAG || '''' ||
                                  ' and test_admin_id = ' || VTESTADMINID ||
                                  ' and form_assignment = ' ||
                                  V_ARRAY_FORM(I) || ' and student_id in (' ||
                                  V_TEMP_STU_LIST || ' )
                           union
                           select distinct test_roster_id, null as default_value
                             from test_roster
                            where test_admin_id = ' ||
                                  VTESTADMINID || ' and form_assignment = ' ||
                                  V_ARRAY_FORM(I) || ' and student_id in (' ||
                                  V_TEMP_STU_LIST || '))
                    group by test_roster_id)';
                V_PREV_INDEX    := V_STR_INDEX;
                V_START_POS     := V_PREV_INDEX + 1;
                V_TEMP_STU_LIST := '';
              END LOOP;

            ELSE
              /*dbms_output.put_line(V_STU_COUNT ||
              '..old logic will work...');*/
              EXECUTE IMMEDIATE 'insert into student_item_set_status
                (test_roster_id,
                 item_set_id,
                 completion_status,
                 validation_status,
                 time_expired,
                 item_set_order,
                 customer_flag_status)
                (select distinct test_roster_id,' ||
                                RECTDITEMSETID.ITEM_SET_ID || ',' || '''' ||
                                TEST_COMPLETION_STATUS || '''' || ',' || '''' ||
                                VALIDATION_STATUS || '''' || ',' || '''' ||
                                TEST_TIME_EXPIRED || '''' || ',' ||
                                VTDITEMSETORDERFORM || ',
                                 max(default_value) as default_value
                   from (select distinct test_roster_id, cc.default_value
                           from test_roster, customer_configuration cc
                          where cc.customer_id = ' ||
                                VCUSTOMERID || '
                            and cc.customer_configuration_name = ' || '''' ||
                                VROSTER_STATUS_FLAG || '''' ||
                                ' and test_admin_id = ' || VTESTADMINID ||
                                ' and form_assignment = ' ||
                                V_ARRAY_FORM(I) || ' and student_id in (' ||
                                VSTUDENTLIST || ' )
                         union
                         select distinct test_roster_id, null as default_value
                           from test_roster
                          where test_admin_id = ' ||
                                VTESTADMINID || ' and form_assignment = ' ||
                                V_ARRAY_FORM(I) || ' and student_id in (' ||
                                VSTUDENTLIST || '))
                  group by test_roster_id)';

            END IF;
            /** IN clause limit fix : END **/

            LOG_MESSAGE(V_LOG_ID,
                        V_STATUS_SUCCESS,
                        'For Grade:' || RECTCITEMSETID.GRADE ||
                        ', For School : ' || NODE_NAME /*RECSCHOOLNODE.ORG_NODE_NAME*/
                        ||
                        ', Test Assignment is Successful for  This test :' ||
                        RECTCITEMSETID.ITEM_SET_DISPLAY_NAME ||
                        ', no Of Students are Scheduled :' ||
                        (LENGTH(VSTUDENTLIST) -
                        LENGTH(REPLACE(VSTUDENTLIST, ',')) + 1));

            VTDITEMSETORDERFORM := VTDITEMSETORDERFORM + 1;
          END LOOP;
        END LOOP;
        VTDITEMSETORDER := VTDITEMSETORDERFORM;
        IF (NOT V_IS_CURSOR_RETURNS_VALUE) THEN
          -- LOGGING ONLY, WHEN CURSOR "crsGetTDTestElementList" DOES NOT RETURNS ANY VALUE
          LOG_MESSAGE(V_LOG_ID,
                      V_STATUS_WARN,
                      'No valid Item found for itemset id [' ||
                      RECTSITEMSETID.ITEM_SET_ID || '] with type TD.');
          V_IS_CURSOR_RETURNS_VALUE := TRUE; -- SETTING TRUE AS PREVIOUS LOOP IS EXECUTING
        END IF;

      ELSE
        LOG_MESSAGE(V_LOG_ID,
                    V_STATUS_WARN,
                    'No Test Assignment is Successful for  This test :' ||
                    RECTCITEMSETID.ITEM_SET_DISPLAY_NAME ||
                    ', Reason no  Students are there in the school or Already the students are scheduled ');
      END IF;
    END LOOP;

    IF (NOT V_IS_CURSOR_RETURNS_VALUE) THEN
      -- LOGGING ONLY, WHEN CURSOR "crsGetSchedulableUnitsForTest" DOES NOT RETURNS ANY VALUE
      LOG_MESSAGE(V_LOG_ID,
                  V_STATUS_WARN,
                  'No valid Item found for itemset id [' ||
                  RECTCITEMSETID.ITEM_SET_ID || '] with type TS.');
      -- IF YOU DO NOT WISH THROW ERROR THEN SET TO  TRUE "V_IS_CURSOR_RETURNS_VALUE" AS PREVIOUS LOOP IS EXECUTING
      RAISE_APPLICATION_ERROR(-20000,
                              'No valid Item found for itemset id [' ||
                              RECTCITEMSETID.ITEM_SET_ID ||
                              '] with type TS.');
    END IF;

  END INSERT_SCHEDULABLE_UNITS_RR;

  PROCEDURE PROCESS_EX_CREATE_TEST_SESSION(VCUSTOMERID    INTEGER,
                                     VPRODUCTID     INTEGER,
                                     SCHEDULINGTYPE VARCHAR2,
                                     INTESTCATALOGID INTEGER) IS

    VTESTADMINID              TEST_ADMIN.TEST_ADMIN_ID%TYPE := NULL;
    VTESTAMINACCESSCODE       TEST_ADMIN.ACCESS_CODE%TYPE := NULL;
    VCREATEDBY                USERS.USER_ID%TYPE := DEFAULT_CREATED_BY;
    VPRODUCTNAME              PRODUCT.PRODUCT_NAME%TYPE := NULL;
    VSHOWSTUDENTFEEDBACK      PRODUCT.SHOW_STUDENT_FEEDBACK%TYPE := NULL;
    VCONFIGDATA               TEST_SCHEDULE_CONFIG%ROWTYPE;
    VTESTCATALOGID            TEST_CATALOG.TEST_CATALOG_ID %TYPE := NULL;
    VCUSTOMERFLAGSTATUS       CUSTOMER_CONFIGURATION.DEFAULT_VALUE%TYPE;
    V_DIFF_START_DATE         NUMBER(8);
    V_DIFF_END_DATE           NUMBER(8);
    V_DIFF_STSRT_END_DATE     NUMBER(8);
    VSTUDENTLIST              VARCHAR2(32767) := '';
    V_REC_COUNT               NUMBER(10) := 0;
    V_IS_CURSOR_RETURNS_VALUE BOOLEAN := FALSE;
    V_CORPORATE_CODE          ORG_NODE.ORG_NODE_CODE%TYPE := NULL;
    EXCLUDED_CODE             BOOLEAN := FALSE;
    VROLEID                   USER_ROLE.ROLE_ID%TYPE;

    /*
      Retrieve all school level org_node_id  of ISTEP customer
    */
    CURSOR CRSGETSCHOOLLVLNODEFORCUSTOMER(VCUSTOMERID INTEGER) IS
      SELECT NODE.ORG_NODE_ID,
             NODE.ORG_NODE_NAME,
             CAT.CATEGORY_NAME,
             NODE.ORG_NODE_CODE
        FROM ORG_NODE NODE, ORG_NODE_CATEGORY CAT
       WHERE CAT.ORG_NODE_CATEGORY_ID = NODE.ORG_NODE_CATEGORY_ID
         AND CAT.CATEGORY_LEVEL = SCHOOL_CATEGORY_LEVEL
         AND NODE.CUSTOMER_ID = VCUSTOMERID
         AND EXISTS
       (SELECT *
                FROM ORG_NODE_PARENT
               WHERE PARENT_ORG_NODE_ID = NODE.ORG_NODE_ID);

    CURSOR CRSGETADMINUSER(VSCHOOLNODEID ORG_NODE.ORG_NODE_ID%TYPE) IS
      SELECT USERS.USER_ID, UROLE.ROLE_ID
        FROM USERS, USER_ROLE UROLE, ROLE R
       WHERE UROLE.USER_ID = USERS.USER_ID
         AND UROLE.ACTIVATION_STATUS = V_ACTIVATION_STATUS
         AND UROLE.ROLE_ID = R.ROLE_ID
         AND R.ROLE_NAME IN ('ADMINISTRATOR', 'ADMINISTRATIVE COORDINATOR')
         AND UROLE.ORG_NODE_ID = VSCHOOLNODEID
         AND users.activation_status = 'AC'
       ORDER BY UROLE.ROLE_ID;

    CURSOR CRSGETCOORDUSER(VSCHOOLNODEID ORG_NODE.ORG_NODE_ID%TYPE) IS
      SELECT USERS.USER_ID, UROLE.ROLE_ID
        FROM USERS, USER_ROLE UROLE, ROLE R
       WHERE UROLE.USER_ID = USERS.USER_ID
         AND UROLE.ACTIVATION_STATUS = V_ACTIVATION_STATUS
         AND UROLE.ROLE_ID = R.ROLE_ID
         AND R.ROLE_NAME = ('COORDINATOR')
         AND UROLE.ORG_NODE_ID = VSCHOOLNODEID
         AND users.activation_status = 'AC'
       ORDER BY UROLE.ROLE_ID;

  BEGIN

    LOG_MESSAGE(V_LOG_ID,
                V_STATUS_START,
                'Procedure started with Product Id [' || VPRODUCTID ||
                '] and Customer Id [' || VCUSTOMERID || '].');
    --------------------------------------------------------------
    ---------  VALIDATION OF BASIC CONFIGURATION -----------------
    --------------------------------------------------------------

    ---  validation of customer_id
    BEGIN

      SELECT 1
        INTO V_REC_COUNT
        FROM CUSTOMER
       WHERE CUSTOMER.CUSTOMER_ID = VCUSTOMERID;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        LOG_MESSAGE(V_LOG_ID,
                    V_STATUS_ERROR,
                    'Customer id [' || VCUSTOMERID ||
                    '] is not valid. Give Existing Id.');
        RAISE_APPLICATION_ERROR(-20000,
                                'Customer id [' || VCUSTOMERID ||
                                '] is not valid. Give Existing Id.');
    END;

    -------------------------------------------------------------
    ---  validation of PRODUCT_ID
    BEGIN
      /* IF ((VPRODUCTID IS NULL) OR (VPRODUCTID <> OPERATIONAL_PRODUCT_ID AND
         VPRODUCTID <> PRACTICE_PRODUCT_ID AND
         VPRODUCTID <> IREAD_PRACTICE_PRODUCT_ID AND
         VPRODUCTID <> IREAD_OPRTIONL_PRODUCT_ID AND
         VPRODUCTID <> SCIENCE_PRODUCT_ID)) THEN
        LOG_MESSAGE(V_LOG_ID,
                    V_STATUS_ERROR,
                    'Only ISTEP Product id is allowed. Product id [' ||
                    VPRODUCTID || '] is not invalid.');
        RAISE_APPLICATION_ERROR(-20000,
                                'Only ISTEP Product id is allowed.');
      END IF;*/

      SELECT PRO.PRODUCT_NAME, PRO.SHOW_STUDENT_FEEDBACK
        INTO VPRODUCTNAME, VSHOWSTUDENTFEEDBACK
        FROM PRODUCT PRO
       WHERE PRO.PRODUCT_ID = VPRODUCTID;

    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        LOG_MESSAGE(V_LOG_ID,
                    V_STATUS_ERROR,
                    'Product id [' || VPRODUCTID ||
                    '] is not valid. Give Existing Id.');
        RAISE_APPLICATION_ERROR(-20000,
                                'Product id [' || VPRODUCTID ||
                                '] is not valid. Give Existing Id.');
    END;
    
        ---  validation of test_catalog_id
    BEGIN

      SELECT 1
       INTO V_REC_COUNT
       FROM TEST_CATALOG
       WHERE TEST_CATALOG_ID = INTESTCATALOGID
       AND PRODUCT_ID = VPRODUCTID;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        LOG_MESSAGE(V_LOG_ID,
                    V_STATUS_ERROR,
                    'Test Catalog id [' || INTESTCATALOGID ||
                    '] is not valid. Give Valid Id.');
        RAISE_APPLICATION_ERROR(-20000,
                                'Test Catalog id [' || INTESTCATALOGID ||
                                '] is not valid. Give Valid Id.');
    END;

    -------------------------------------------------------------
    -- validation of config data
    BEGIN
      SELECT *
        INTO VCONFIGDATA
        FROM TEST_SCHEDULE_CONFIG TSC
       WHERE CUSTOMER_ID = VCUSTOMERID;
      V_DIFF_START_DATE     := (TO_DATE(TO_CHAR(VCONFIGDATA.LOGIN_START_DATE,
                                                'YYYY-MM-DD'),
                                        'YYYY-MM-DD') -
                               TO_DATE(TO_CHAR(SYSDATE, 'YYYY-MM-DD'),
                                        'YYYY-MM-DD'));
      V_DIFF_END_DATE       := (TO_DATE(TO_CHAR(VCONFIGDATA.LOGIN_END_DATE,
                                                'YYYY-MM-DD'),
                                        'YYYY-MM-DD') -
                               TO_DATE(TO_CHAR(SYSDATE, 'YYYY-MM-DD'),
                                        'YYYY-MM-DD'));
      V_DIFF_STSRT_END_DATE := (VCONFIGDATA.LOGIN_END_DATE -
                               VCONFIGDATA.LOGIN_START_DATE);

      IF (V_DIFF_START_DATE IS NULL OR V_DIFF_END_DATE IS NULL) THEN
        LOG_MESSAGE(V_LOG_ID,
                    V_STATUS_ERROR,
                    'Schedule start date or end date can not be null.');
        RAISE_APPLICATION_ERROR(-20000,
                                'Schedule start date or end date can not be null.');
      ELSIF (V_DIFF_START_DATE < 0) THEN
        LOG_MESSAGE(V_LOG_ID,
                    V_STATUS_ERROR,
                    'Schedule start date can not be earlier than now.');
        RAISE_APPLICATION_ERROR(-20000,
                                'Schedule start date can not be earlier than now.');
      ELSIF (V_DIFF_END_DATE < 0) THEN
        LOG_MESSAGE(V_LOG_ID,
                    V_STATUS_ERROR,
                    'Schedule end date can not be earlier than now.');
        RAISE_APPLICATION_ERROR(-20000,
                                'Schedule end date can not be earlier than now.');
      ELSIF (V_DIFF_STSRT_END_DATE < 0) THEN
        LOG_MESSAGE(V_LOG_ID,
                    V_STATUS_ERROR,
                    'Schedule  end date can not be earlier than Schedule start date.');
        RAISE_APPLICATION_ERROR(-20000,
                                'Schedule  end date can not be earlier than Schedule start date.');
      ELSIF (V_DIFF_STSRT_END_DATE = 0) THEN
        IF ((TO_DATE(TO_CHAR(VCONFIGDATA.DAILY_LOGIN_END_TIME, 'HH24:MI'),
                     'HH24:MI')) -
           (TO_DATE(TO_CHAR(VCONFIGDATA.DAILY_LOGIN_START_TIME, 'HH24:MI'),
                     'HH24:MI')) <= 0) THEN

          LOG_MESSAGE(V_LOG_ID,
                      V_STATUS_ERROR,
                      'Schedule  end time can not be earlier than Schedule start time.');
          RAISE_APPLICATION_ERROR(-20000,
                                  'Schedule  end time can not be earlier than Schedule start time.');
        END IF;

      END IF;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        LOG_MESSAGE(V_LOG_ID,
                    V_STATUS_ERROR,
                    'No record exist in configuration table for customer id [' ||
                    VCUSTOMERID || '] ');
        RAISE_APPLICATION_ERROR(-20000,
                                'No record exist in configuration table for customer id [' ||
                                VCUSTOMERID || '] ');
      WHEN TOO_MANY_ROWS THEN
        LOG_MESSAGE(V_LOG_ID,
                    V_STATUS_ERROR,
                    'More than one record exists in configuration table for customer id [' ||
                    VCUSTOMERID || '] ');
        RAISE_APPLICATION_ERROR(-20000,
                                'More than one record exists in configuration table for customer id [' ||
                                VCUSTOMERID || '] ');
    END;

    V_ARRAY6_8(1) := '06';
    V_ARRAY6_8(2) := '07';
    V_ARRAY6_8(3) := '08';
    --------------------------------------------------------------
    --------------------Retrive Roster_Status_Flag----------------
    --------------------------------------------------------------
    BEGIN
      SELECT CC.DEFAULT_VALUE
        INTO VCUSTOMERFLAGSTATUS
        FROM CUSTOMER_CONFIGURATION CC
       WHERE CC.CUSTOMER_ID = VCUSTOMERID
         AND CC.CUSTOMER_CONFIGURATION_NAME = 'Roster_Status_Flag';

    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        LOG_MESSAGE(V_LOG_ID,
                    V_STATUS_WARN,
                    'CustomerFlagStatus not found for customer[' ||
                    VCUSTOMERID ||
                    '] with configuration name- Roster_Status_Flag  ');

      WHEN TOO_MANY_ROWS THEN
        LOG_MESSAGE(V_LOG_ID,
                    V_STATUS_ERROR,
                    'Too many CustomerFlagStatus found for customer [' ||
                    VCUSTOMERID ||
                    '] with configuration name- Roster_Status_Flag .');
        RAISE_APPLICATION_ERROR(-20000,
                                'Too many CustomerFlagStatus found for customer [' ||
                                VCUSTOMERID ||
                                '] with configuration name- Roster_Status_Flag .');
      WHEN OTHERS THEN
        LOG_MESSAGE(V_LOG_ID,
                    V_STATUS_ERROR,
                    'Exception occurred while retrieving CustomerFlagStatus of customer [' ||
                    VCUSTOMERID || '].');

        RAISE;
    END;
    --------------------------------------------------------------
    -----------------    PROCESS STARTING  -----------------------
    --------------------------------------------------------------

    ---------- FOR LOOP 1   ------
    V_IS_CURSOR_RETURNS_VALUE := FALSE;
    FOR RECSCHOOLNODE IN CRSGETSCHOOLLVLNODEFORCUSTOMER(VCUSTOMERID) LOOP

      -- to get detail of user associated at top org node id  for created_by
      V_IS_CURSOR_RETURNS_VALUE := TRUE;
      VCREATEDBY                := NULL;
      IF RECSCHOOLNODE.ORG_NODE_ID IS NOT NULL THEN

        BEGIN

          OPEN CRSGETADMINUSER(RECSCHOOLNODE.ORG_NODE_ID);

          FETCH CRSGETADMINUSER
            INTO VCREATEDBY, VROLEID;

          IF CRSGETADMINUSER%NOTFOUND THEN

            OPEN CRSGETCOORDUSER(RECSCHOOLNODE.ORG_NODE_ID);
            FETCH CRSGETCOORDUSER
              INTO VCREATEDBY, VROLEID;

            IF CRSGETCOORDUSER%NOTFOUND THEN

              RAISE NO_DATA_FOUND;

            END IF;

            CLOSE CRSGETCOORDUSER;
          END IF;

          CLOSE CRSGETADMINUSER;

        EXCEPTION
          -- OTHER THAN NO_DATA_FOUND EXCEPTION IS THROWN
          WHEN NO_DATA_FOUND THEN
            LOG_MESSAGE(V_LOG_ID,
                        V_STATUS_WARN,
                        'No Administrator is found for Org id [' ||
                        RECSCHOOLNODE.ORG_NODE_ID || '].');
            IF CRSGETCOORDUSER%ISOPEN THEN
              CLOSE CRSGETCOORDUSER;
            END IF;

            IF CRSGETADMINUSER%ISOPEN THEN
              CLOSE CRSGETADMINUSER;
            END IF;

          WHEN OTHERS THEN
            LOG_MESSAGE(V_LOG_ID,
                        V_STATUS_ERROR,
                        'Exception occurred while retrieving Administrator information for Org id [' ||
                        RECSCHOOLNODE.ORG_NODE_ID || '].');

            IF CRSGETCOORDUSER%ISOPEN THEN
              CLOSE CRSGETCOORDUSER;
            END IF;

            IF CRSGETADMINUSER%ISOPEN THEN
              CLOSE CRSGETADMINUSER;
            END IF;

            RAISE;
        END;

      END IF;

      IF (VCREATEDBY IS NOT NULL) THEN
        -------------------------------------------------------
        -- Retrieving productname for testadmin name
        -------------------------------------------------------
        /*BEGIN
          SELECT PRO.PRODUCT_NAME, PRO.SHOW_STUDENT_FEEDBACK
            INTO VPRODUCTNAME, VSHOWSTUDENTFEEDBACK
            FROM PRODUCT PRO
           WHERE PRO.PRODUCT_ID = VPRODUCTID;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            LOG_MESSAGE(V_LOG_ID,
                        V_STATUS_ERROR,
                        'Product id [' || VPRODUCTID ||
                        '] does not exists.');
            RAISE_APPLICATION_ERROR(-20000,
                                    'Product [' || VPRODUCTID ||
                                    '] does not exists.');
          WHEN OTHERS THEN
            LOG_MESSAGE(V_LOG_ID,
                        V_STATUS_ERROR,
                        'Exception occurred while retrieving product.id [' ||
                        VPRODUCTID || ']');
            RAISE;
        END;*/

        IF (RECSCHOOLNODE.ORG_NODE_CODE IS NOT NULL) THEN
          BEGIN
            SELECT ORG.ORG_NODE_CODE
              INTO V_CORPORATE_CODE
              FROM ORG_NODE_PARENT ONP, ORG_NODE ORG
             WHERE ORG.ORG_NODE_ID = ONP.PARENT_ORG_NODE_ID
               AND ONP.ORG_NODE_ID = RECSCHOOLNODE.ORG_NODE_ID;

          EXCEPTION
            WHEN NO_DATA_FOUND THEN
              EXCLUDED_CODE := FALSE;

          END;
        ELSE
          EXCLUDED_CODE := FALSE;
        END IF;

        --------- FOR LOOP 1.1 ----------------

        V_IS_CURSOR_RETURNS_VALUE := FALSE;

        FOR RECTCITEMSETID IN CRSGETTCTESTFORPRODUCT(VPRODUCTID, VCREATEDBY, INTESTCATALOGID) LOOP
          V_IS_CURSOR_RETURNS_VALUE := TRUE;

          EXCLUDED_CODE := FALSE;
          /*IF (V_CORPORATE_CODE IS NOT NULL) THEN

            SELECT DECODE(COUNT(*), 0, 1, 0)
              INTO NODE_EXCLUDED
              FROM EXCLUDE_TEST_SCHE_NODE EXSN
             WHERE EXSN.CUSTOMER_ID = VCUSTOMERID
               AND EXSN.CORPORATE_ID = V_CORPORATE_CODE
               AND EXSN.SCHOOL_ID = RECSCHOOLNODE.ORG_NODE_CODE
               AND EXSN.GRADE = RECTCITEMSETID.GRADE;

            IF (NODE_EXCLUDED = 0) THEN
              EXCLUDED_CODE := TRUE;
            END IF;
          END IF;

          IF (NOT EXCLUDED_CODE) THEN
            BEGIN
              SELECT DISTINCT CAT.TEST_CATALOG_ID
                INTO VTESTCATALOGID
                FROM TEST_CATALOG CAT
               WHERE ACTIVATION_STATUS = 'AC'
                 AND ITEM_SET_ID = RECTCITEMSETID.ITEM_SET_ID;
            EXCEPTION
              WHEN NO_DATA_FOUND THEN
                LOG_MESSAGE(V_LOG_ID,
                            V_STATUS_ERROR,
                            'Catalog does not exists with item set id [' ||
                            RECTCITEMSETID.ITEM_SET_ID || '].');
                RAISE_APPLICATION_ERROR(-20000,
                                        'Catalog does not exists with item set id [' ||
                                        RECTCITEMSETID.ITEM_SET_ID || '].');
              WHEN TOO_MANY_ROWS THEN
                LOG_MESSAGE(V_LOG_ID,
                            V_STATUS_ERROR,
                            'Too many catalog associated with item set id [' ||
                            RECTCITEMSETID.ITEM_SET_ID || '].');
                RAISE_APPLICATION_ERROR(-20000,
                                        'Too many catalog associated with item set id [' ||
                                        RECTCITEMSETID.ITEM_SET_ID || '].');
              WHEN OTHERS THEN
                LOG_MESSAGE(V_LOG_ID,
                            V_STATUS_ERROR,
                            'Exception occurred while retrieving catalog id [' ||
                            RECTCITEMSETID.ITEM_SET_ID || '].');

                RAISE;
            END;
           END IF;*/

          IF (TRIM(RECTCITEMSETID.GRADE) = '6-8') THEN
            ----------
            FOR I IN V_ARRAY6_8.FIRST .. V_ARRAY6_8.LAST LOOP

              IF (V_CORPORATE_CODE IS NOT NULL) THEN

                SELECT DECODE(COUNT(*), 0, 1, 0)
                  INTO NODE_EXCLUDED
                  FROM EXCLUDE_TEST_SCHE_NODE EXSN
                 WHERE EXSN.CUSTOMER_ID = VCUSTOMERID
                   AND EXSN.CORPORATE_ID = V_CORPORATE_CODE
                   AND EXSN.SCHOOL_ID = RECSCHOOLNODE.ORG_NODE_CODE
                   AND EXSN.GRADE = V_ARRAY6_8(I);

                IF (NODE_EXCLUDED = 0) THEN
                  EXCLUDED_CODE := TRUE;
                END IF;
              END IF;

              IF (NOT EXCLUDED_CODE) THEN

                BEGIN
                  SELECT DISTINCT CAT.TEST_CATALOG_ID
                    INTO VTESTCATALOGID
                    FROM TEST_CATALOG CAT
                   WHERE ACTIVATION_STATUS = 'AC'
                     AND ITEM_SET_ID = RECTCITEMSETID.ITEM_SET_ID;
                EXCEPTION
                  WHEN NO_DATA_FOUND THEN
                    LOG_MESSAGE(V_LOG_ID,
                                V_STATUS_ERROR,
                                'Catalog does not exists with item set id [' ||
                                RECTCITEMSETID.ITEM_SET_ID || '].');
                    RAISE_APPLICATION_ERROR(-20000,
                                            'Catalog does not exists with item set id [' ||
                                            RECTCITEMSETID.ITEM_SET_ID || '].');
                  WHEN TOO_MANY_ROWS THEN
                    LOG_MESSAGE(V_LOG_ID,
                                V_STATUS_ERROR,
                                'Too many catalog associated with item set id [' ||
                                RECTCITEMSETID.ITEM_SET_ID || '].');
                    RAISE_APPLICATION_ERROR(-20000,
                                            'Too many catalog associated with item set id [' ||
                                            RECTCITEMSETID.ITEM_SET_ID || '].');
                  WHEN OTHERS THEN
                    LOG_MESSAGE(V_LOG_ID,
                                V_STATUS_ERROR,
                                'Exception occurred while retrieving catalog id [' ||
                                RECTCITEMSETID.ITEM_SET_ID || '].');

                    RAISE;
                END;

                OPEN CRSGETSTUFORALLGRADEATSCHOOL(V_ARRAY6_8(I),
                                                  RECSCHOOLNODE.ORG_NODE_ID,
                                                  VPRODUCTID, INTESTCATALOGID);
                FETCH CRSGETSTUFORALLGRADEATSCHOOL
                  INTO V_STUDENT;
                IF (CRSGETSTUFORALLGRADEATSCHOOL%FOUND) THEN

                  CLOSE CRSGETSTUFORALLGRADEATSCHOOL;

                  VTESTAMINACCESSCODE := GENERATE_TAC(V_GENERATED_ACCESS_CODE_LENGTH,
                                                      1);

                  SELECT SEQ_TEST_ADMIN_ID.NEXTVAL
                    INTO VTESTADMINID
                    FROM DUAL;

                  IF SCHEDULINGTYPE = 'roundrobin' THEN

                    INSERT_TEST_ADMIN(VPRODUCTID,
                                      VCUSTOMERID,
                                      RECTCITEMSETID,
                                      VCONFIGDATA,
                                      VSHOWSTUDENTFEEDBACK,
                                      VTESTCATALOGID,
                                      VCREATEDBY,
                                      VTESTAMINACCESSCODE,
                                      RECSCHOOLNODE.ORG_NODE_ID,
                                      VTESTADMINID);

                    V_REC_COUNT := 0;
                    INSERT_ALL_STU_AT_SCHOOL_RR(VPRODUCTID,
                                                VCUSTOMERID,
                                                VCREATEDBY,
                                                VTESTADMINID,
                                                V_ARRAY6_8(I),
                                                RECSCHOOLNODE.ORG_NODE_ID,
                                                VSTUDENTLIST,
                                                VCUSTOMERFLAGSTATUS,
                                                INTESTCATALOGID);

                    INSERT_SCHEDULABLE_UNITS_RR(VCUSTOMERID,
                                                RECTCITEMSETID,
                                                RECSCHOOLNODE.ORG_NODE_NAME,
                                                VTESTADMINID,
                                                VTESTAMINACCESSCODE,
                                                VSTUDENTLIST);

                  ELSE
                    INSERT_TEST_ADMIN(VPRODUCTID,
                                      VCUSTOMERID,
                                      RECTCITEMSETID,
                                      VCONFIGDATA,
                                      VSHOWSTUDENTFEEDBACK,
                                      VTESTCATALOGID,
                                      VCREATEDBY,
                                      VTESTAMINACCESSCODE,
                                      RECSCHOOLNODE.ORG_NODE_ID,
                                      VTESTADMINID);

                    V_REC_COUNT := 0;
                    INSERT_ALL_STU_AT_SCHOOL(VPRODUCTID,
                                             VCUSTOMERID,
                                             VCREATEDBY,
                                             VTESTADMINID,
                                             V_ARRAY6_8(I),
                                             RECSCHOOLNODE.ORG_NODE_ID,
                                             VSTUDENTLIST,
                                             VCUSTOMERFLAGSTATUS,
                                             INTESTCATALOGID);

                    INSERT_SCHEDULABLE_UNITS(VCUSTOMERID,
                                             RECTCITEMSETID,
                                             RECSCHOOLNODE.ORG_NODE_NAME,
                                             VTESTADMINID,
                                             VTESTAMINACCESSCODE,
                                             VSTUDENTLIST);
                  END IF;
                  ---------- FOR LOOP 1.1.1  ------
                  /*
                      Insert the itemset record associated with test(TC) of test session
                  */
                  VSTUDENTLIST := '';

                ELSE
                  CLOSE CRSGETSTUFORALLGRADEATSCHOOL;
                END IF;
              END IF;
            END LOOP;

          ELSE
            IF (V_CORPORATE_CODE IS NOT NULL) THEN
            EXCLUDED_CODE := FALSE; --Changed for Prescheduling story 1549
              SELECT DECODE(COUNT(*), 0, 1, 0)
                INTO NODE_EXCLUDED
                FROM EXCLUDE_TEST_SCHE_NODE EXSN
               WHERE EXSN.CUSTOMER_ID = VCUSTOMERID
                 AND EXSN.CORPORATE_ID = V_CORPORATE_CODE
                 AND EXSN.SCHOOL_ID = RECSCHOOLNODE.ORG_NODE_CODE
                 AND EXSN.GRADE = RECTCITEMSETID.GRADE;

              IF (NODE_EXCLUDED = 0) THEN
                EXCLUDED_CODE := TRUE;
              END IF;
            END IF;

            BEGIN
              SELECT DISTINCT CAT.TEST_CATALOG_ID
                INTO VTESTCATALOGID
                FROM TEST_CATALOG CAT
               WHERE ACTIVATION_STATUS = 'AC'
                 AND ITEM_SET_ID = RECTCITEMSETID.ITEM_SET_ID;

            EXCEPTION
              WHEN NO_DATA_FOUND THEN
                LOG_MESSAGE(V_LOG_ID,
                            V_STATUS_ERROR,
                            'Catalog does not exists with item set id [' ||
                            RECTCITEMSETID.ITEM_SET_ID || '].');
                RAISE_APPLICATION_ERROR(-20000,
                                        'Catalog does not exists with item set id [' ||
                                        RECTCITEMSETID.ITEM_SET_ID || '].');
              WHEN TOO_MANY_ROWS THEN
                LOG_MESSAGE(V_LOG_ID,
                            V_STATUS_ERROR,
                            'Too many catalog associated with item set id [' ||
                            RECTCITEMSETID.ITEM_SET_ID || '].');
                RAISE_APPLICATION_ERROR(-20000,
                                        'Too many catalog associated with item set id [' ||
                                        RECTCITEMSETID.ITEM_SET_ID || '].');
              WHEN OTHERS THEN
                LOG_MESSAGE(V_LOG_ID,
                            V_STATUS_ERROR,
                            'Exception occurred while retrieving catalog id [' ||
                            RECTCITEMSETID.ITEM_SET_ID || '].');

                RAISE;
            END;
            IF (NOT EXCLUDED_CODE) THEN

              OPEN CRSGETSTUFORALLGRADEATSCHOOL(RECTCITEMSETID.GRADE,
                                                RECSCHOOLNODE.ORG_NODE_ID,
                                                VPRODUCTID, INTESTCATALOGID);
              FETCH CRSGETSTUFORALLGRADEATSCHOOL
                INTO V_STUDENT;
              IF (CRSGETSTUFORALLGRADEATSCHOOL%FOUND) THEN

                CLOSE CRSGETSTUFORALLGRADEATSCHOOL;

                VTESTAMINACCESSCODE := GENERATE_TAC(V_GENERATED_ACCESS_CODE_LENGTH,
                                                    1);

                SELECT SEQ_TEST_ADMIN_ID.NEXTVAL
                  INTO VTESTADMINID
                  FROM DUAL;

                INSERT_TEST_ADMIN(VPRODUCTID,
                                  VCUSTOMERID,
                                  RECTCITEMSETID,
                                  VCONFIGDATA,
                                  VSHOWSTUDENTFEEDBACK,
                                  VTESTCATALOGID,
                                  VCREATEDBY,
                                  VTESTAMINACCESSCODE,
                                  RECSCHOOLNODE.ORG_NODE_ID,
                                  VTESTADMINID);

                V_REC_COUNT := 0;

                IF SCHEDULINGTYPE = 'roundrobin' THEN
                  INSERT_ALL_STU_AT_SCHOOL_RR(VPRODUCTID,
                                              VCUSTOMERID,
                                              VCREATEDBY,
                                              VTESTADMINID,
                                              RECTCITEMSETID.GRADE,
                                              RECSCHOOLNODE.ORG_NODE_ID,
                                              VSTUDENTLIST,
                                              VCUSTOMERFLAGSTATUS,
                                              INTESTCATALOGID);

                  INSERT_SCHEDULABLE_UNITS_RR(VCUSTOMERID,
                                              RECTCITEMSETID,
                                              RECSCHOOLNODE.ORG_NODE_NAME,
                                              VTESTADMINID,
                                              VTESTAMINACCESSCODE,
                                              VSTUDENTLIST);
                ELSE
                  INSERT_ALL_STU_AT_SCHOOL(VPRODUCTID,
                                           VCUSTOMERID,
                                           VCREATEDBY,
                                           VTESTADMINID,
                                           RECTCITEMSETID.GRADE,
                                           RECSCHOOLNODE.ORG_NODE_ID,
                                           VSTUDENTLIST,
                                           VCUSTOMERFLAGSTATUS,
                                           INTESTCATALOGID);

                  INSERT_SCHEDULABLE_UNITS(VCUSTOMERID,
                                           RECTCITEMSETID,
                                           RECSCHOOLNODE.ORG_NODE_NAME,
                                           VTESTADMINID,
                                           VTESTAMINACCESSCODE,
                                           VSTUDENTLIST);
                END IF;
                ---------- FOR LOOP 1.1.1  ------
                /*
                    Insert the itemset record associated with test(TC) of test session
                */

                VSTUDENTLIST := '';

              ELSE
                CLOSE CRSGETSTUFORALLGRADEATSCHOOL;
              END IF;
            END IF;
          END IF;
          -- END IF;
        END LOOP;

        /*  IF (NOT V_IS_CURSOR_RETURNS_VALUE) THEN
          -- LOGGING ONLY, WHEN CURSOR "crsGetTCTestForProduct" DOES NOT RETURNS ANY VALUE
          LOG_MESSAGE(V_LOG_ID,
                      V_STATUS_ERROR,
                      'No valid catalog found for product id [' ||
                      VPRODUCTID || '] and user id [' || VCREATEDBY || '].');
          -- IF YOU DO NOT WISH THROW ERROR THEN SET TO  TRUE "V_IS_CURSOR_RETURNS_VALUE" AS PREVIOUS LOOP IS EXECUTING
          RAISE_APPLICATION_ERROR(-20000,
                                  'No valid catalog found for product id [' ||
                                  VPRODUCTID || '] and user id [' ||
                                  VCREATEDBY || '].');
        END IF;*/

        COMMIT; -- need to remove comment before QA delivery
      END IF;
    END LOOP;
    IF (NOT V_IS_CURSOR_RETURNS_VALUE) THEN
      -- LOGGING ONLY, WHEN CURSOR "crsGetSchoollvlNodeForCustomer" DOES NOT RETURNS ANY VALUE
      LOG_MESSAGE(V_LOG_ID,
                  V_STATUS_ERROR,
                  'No valid school level org_node_id is found for Customer id [' ||
                  VCUSTOMERID || '].');
      RAISE_APPLICATION_ERROR(-20000,
                              'No valid school level org_node_id is found for Customer id [' ||
                              VCUSTOMERID || '].');
    END IF;

    LOG_MESSAGE(V_LOG_ID,
                V_STATUS_COMPLETE,
                'Procedure completed successfully.');
  EXCEPTION
    WHEN OTHERS THEN
      LOG_MESSAGE(V_LOG_ID,
                  V_STATUS_COMPLETE,
                  'Procedure completed with error:' ||
                  DBMS_UTILITY.FORMAT_ERROR_BACKTRACE);
      ROLLBACK; -- ROLLBACK ALL DATA MODIFIED EXPLICITELY
      RAISE;
  END PROCESS_EX_CREATE_TEST_SESSION;

  PROCEDURE PROCESS_IN_CREATE_TEST_SESSION(VCUSTOMERID    INTEGER,
                                        VPRODUCTID     INTEGER,
                                        SCHEDULINGTYPE VARCHAR2,
                                        INTESTCATALOGID INTEGER) IS

    VTESTADMINID              TEST_ADMIN.TEST_ADMIN_ID%TYPE := NULL;
    VTESTAMINACCESSCODE       TEST_ADMIN.ACCESS_CODE%TYPE := NULL;
    VCREATEDBY                USERS.USER_ID%TYPE := DEFAULT_CREATED_BY;
    VPRODUCTNAME              PRODUCT.PRODUCT_NAME%TYPE := NULL;
    VSHOWSTUDENTFEEDBACK      PRODUCT.SHOW_STUDENT_FEEDBACK%TYPE := NULL;
    VCONFIGDATA               TEST_SCHEDULE_CONFIG%ROWTYPE;
    VTESTCATALOGID            TEST_CATALOG.TEST_CATALOG_ID %TYPE := NULL;
    VCUSTOMERFLAGSTATUS       CUSTOMER_CONFIGURATION.DEFAULT_VALUE%TYPE;
    V_DIFF_START_DATE         NUMBER(8);
    V_DIFF_END_DATE           NUMBER(8);
    V_DIFF_STSRT_END_DATE     NUMBER(8);
    VSTUDENTLIST              VARCHAR2(32767) := '';
    V_REC_COUNT               NUMBER(10) := 0;
    V_IS_CURSOR_RETURNS_VALUE BOOLEAN := FALSE;
    V_CORPORATE_CODE          ORG_NODE.ORG_NODE_CODE%TYPE := NULL;
    INCLUDED_CODE             BOOLEAN := FALSE;
    V_ITEM_SET_ID             ITEM_SET.ITEM_SET_ID%TYPE := '';
    VROLEID                   USER_ROLE.ROLE_ID%TYPE;
    /*
      Retrieve all school level org_node_id  of ISTEP customer
    */
    CURSOR CRSGETSCHOOLLVLNODEFORCUSTOMER(VCUSTOMERID INTEGER) IS
      SELECT NODE.ORG_NODE_ID,
             NODE.ORG_NODE_NAME,
             CAT.CATEGORY_NAME,
             NODE.ORG_NODE_CODE
        FROM ORG_NODE NODE, ORG_NODE_CATEGORY CAT
       WHERE CAT.ORG_NODE_CATEGORY_ID = NODE.ORG_NODE_CATEGORY_ID
         AND CAT.CATEGORY_LEVEL = SCHOOL_CATEGORY_LEVEL
         AND NODE.CUSTOMER_ID = VCUSTOMERID
         AND EXISTS
       (SELECT *
                FROM ORG_NODE_PARENT
               WHERE PARENT_ORG_NODE_ID = NODE.ORG_NODE_ID);

    CURSOR CRSGETADMINUSER(VSCHOOLNODEID ORG_NODE.ORG_NODE_ID%TYPE) IS
      SELECT USERS.USER_ID, UROLE.ROLE_ID
        FROM USERS, USER_ROLE UROLE, ROLE R
       WHERE UROLE.USER_ID = USERS.USER_ID
         AND UROLE.ACTIVATION_STATUS = V_ACTIVATION_STATUS
         AND UROLE.ROLE_ID = R.ROLE_ID
         AND R.ROLE_NAME IN ('ADMINISTRATOR', 'ADMINISTRATIVE COORDINATOR')
         AND UROLE.ORG_NODE_ID = VSCHOOLNODEID
         AND users.activation_status = 'AC'
       ORDER BY UROLE.ROLE_ID;

    CURSOR CRSGETCOORDUSER(VSCHOOLNODEID ORG_NODE.ORG_NODE_ID%TYPE) IS
      SELECT USERS.USER_ID, UROLE.ROLE_ID
        FROM USERS, USER_ROLE UROLE, ROLE R
       WHERE UROLE.USER_ID = USERS.USER_ID
         AND UROLE.ACTIVATION_STATUS = V_ACTIVATION_STATUS
         AND UROLE.ROLE_ID = R.ROLE_ID
         AND R.ROLE_NAME = ('COORDINATOR')
         AND UROLE.ORG_NODE_ID = VSCHOOLNODEID
         AND users.activation_status = 'AC'
       ORDER BY UROLE.ROLE_ID;

  BEGIN

    LOG_MESSAGE(V_LOG_ID,
                V_STATUS_START,
                'Procedure started with Product Id [' || VPRODUCTID ||
                '] and Customer Id [' || VCUSTOMERID || '].');
    --------------------------------------------------------------
    ---------  VALIDATION OF BASIC CONFIGURATION -----------------
    --------------------------------------------------------------

    ---  validation of customer_id
    BEGIN

      SELECT 1
        INTO V_REC_COUNT
        FROM CUSTOMER
       WHERE CUSTOMER.CUSTOMER_ID = VCUSTOMERID;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        LOG_MESSAGE(V_LOG_ID,
                    V_STATUS_ERROR,
                    'Customer id [' || VCUSTOMERID ||
                    '] is not valid. Give Existing Id.');
        RAISE_APPLICATION_ERROR(-20000,
                                'Customer id [' || VCUSTOMERID ||
                                '] is not valid. Give Existing Id.');
    END;

    -------------------------------------------------------------
    ---  validation of PRODUCT_ID
    BEGIN
      /*IF ((VPRODUCTID IS NULL) OR (VPRODUCTID <> OPERATIONAL_PRODUCT_ID AND
         VPRODUCTID <> PRACTICE_PRODUCT_ID AND
         VPRODUCTID <> IREAD_PRACTICE_PRODUCT_ID AND
         VPRODUCTID <> IREAD_OPRTIONL_PRODUCT_ID AND
         VPRODUCTID <> SCIENCE_PRODUCT_ID)) THEN
        LOG_MESSAGE(V_LOG_ID,
                    V_STATUS_ERROR,
                    'Only ISTEP Product id is allowed. Product id [' ||
                    VPRODUCTID || '] is not invalid.');
        RAISE_APPLICATION_ERROR(-20000,
                                'Only ISTEP Product id is allowed.');
      END IF;*/

      SELECT PRO.PRODUCT_NAME, PRO.SHOW_STUDENT_FEEDBACK
        INTO VPRODUCTNAME, VSHOWSTUDENTFEEDBACK
        FROM PRODUCT PRO
       WHERE PRO.PRODUCT_ID = VPRODUCTID;

    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        LOG_MESSAGE(V_LOG_ID,
                    V_STATUS_ERROR,
                    'Product id [' || VPRODUCTID ||
                    '] is not valid. Give Existing Id.');
        RAISE_APPLICATION_ERROR(-20000,
                                'Product id [' || VPRODUCTID ||
                                '] is not valid. Give Existing Id.');
    END;
    
        ---  validation of test_catalog_id
    BEGIN

      SELECT 1
       INTO V_REC_COUNT
       FROM TEST_CATALOG
       WHERE TEST_CATALOG_ID = INTESTCATALOGID
       AND PRODUCT_ID = VPRODUCTID;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        LOG_MESSAGE(V_LOG_ID,
                    V_STATUS_ERROR,
                    'Test Catalog id [' || INTESTCATALOGID ||
                    '] is not valid. Give Valid Id.');
        RAISE_APPLICATION_ERROR(-20000,
                                'Test Catalog id [' || INTESTCATALOGID ||
                                '] is not valid. Give Valid Id.');
    END;

    -------------------------------------------------------------
    -- validation of config data
    BEGIN
      SELECT *
        INTO VCONFIGDATA
        FROM TEST_SCHEDULE_CONFIG TSC
       WHERE CUSTOMER_ID = VCUSTOMERID;
      V_DIFF_START_DATE     := (TO_DATE(TO_CHAR(VCONFIGDATA.LOGIN_START_DATE,
                                                'YYYY-MM-DD'),
                                        'YYYY-MM-DD') -
                               TO_DATE(TO_CHAR(SYSDATE, 'YYYY-MM-DD'),
                                        'YYYY-MM-DD'));
      V_DIFF_END_DATE       := (TO_DATE(TO_CHAR(VCONFIGDATA.LOGIN_END_DATE,
                                                'YYYY-MM-DD'),
                                        'YYYY-MM-DD') -
                               TO_DATE(TO_CHAR(SYSDATE, 'YYYY-MM-DD'),
                                        'YYYY-MM-DD'));
      V_DIFF_STSRT_END_DATE := (VCONFIGDATA.LOGIN_END_DATE -
                               VCONFIGDATA.LOGIN_START_DATE);

      IF (V_DIFF_START_DATE IS NULL OR V_DIFF_END_DATE IS NULL) THEN
        LOG_MESSAGE(V_LOG_ID,
                    V_STATUS_ERROR,
                    'Schedule start date or end date can not be null.');
        RAISE_APPLICATION_ERROR(-20000,
                                'Schedule start date or end date can not be null.');
      ELSIF (V_DIFF_START_DATE < 0) THEN
        LOG_MESSAGE(V_LOG_ID,
                    V_STATUS_ERROR,
                    'Schedule start date can not be earlier than now.');
        RAISE_APPLICATION_ERROR(-20000,
                                'Schedule start date can not be earlier than now.');
      ELSIF (V_DIFF_END_DATE < 0) THEN
        LOG_MESSAGE(V_LOG_ID,
                    V_STATUS_ERROR,
                    'Schedule end date can not be earlier than now.');
        RAISE_APPLICATION_ERROR(-20000,
                                'Schedule end date can not be earlier than now.');
      ELSIF (V_DIFF_STSRT_END_DATE < 0) THEN
        LOG_MESSAGE(V_LOG_ID,
                    V_STATUS_ERROR,
                    'Schedule  end date can not be earlier than Schedule start date.');
        RAISE_APPLICATION_ERROR(-20000,
                                'Schedule  end date can not be earlier than Schedule start date.');
      ELSIF (V_DIFF_STSRT_END_DATE = 0) THEN
        IF ((TO_DATE(TO_CHAR(VCONFIGDATA.DAILY_LOGIN_END_TIME, 'HH24:MI'),
                     'HH24:MI')) -
           (TO_DATE(TO_CHAR(VCONFIGDATA.DAILY_LOGIN_START_TIME, 'HH24:MI'),
                     'HH24:MI')) <= 0) THEN

          LOG_MESSAGE(V_LOG_ID,
                      V_STATUS_ERROR,
                      'Schedule  end time can not be earlier than Schedule start time.');
          RAISE_APPLICATION_ERROR(-20000,
                                  'Schedule  end time can not be earlier than Schedule start time.');
        END IF;

      END IF;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        LOG_MESSAGE(V_LOG_ID,
                    V_STATUS_ERROR,
                    'No record exist in configuration table for customer id [' ||
                    VCUSTOMERID || '] ');
        RAISE_APPLICATION_ERROR(-20000,
                                'No record exist in configuration table for customer id [' ||
                                VCUSTOMERID || '] ');
      WHEN TOO_MANY_ROWS THEN
        LOG_MESSAGE(V_LOG_ID,
                    V_STATUS_ERROR,
                    'More than one record exists in configuration table for customer id [' ||
                    VCUSTOMERID || '] ');
        RAISE_APPLICATION_ERROR(-20000,
                                'More than one record exists in configuration table for customer id [' ||
                                VCUSTOMERID || '] ');
    END;

    V_ARRAY6_8(1) := '06';
    V_ARRAY6_8(2) := '07';
    V_ARRAY6_8(3) := '08';
    --------------------------------------------------------------
    --------------------Retrive Roster_Status_Flag----------------
    --------------------------------------------------------------
    BEGIN
      SELECT CC.DEFAULT_VALUE
        INTO VCUSTOMERFLAGSTATUS
        FROM CUSTOMER_CONFIGURATION CC
       WHERE CC.CUSTOMER_ID = VCUSTOMERID
         AND CC.CUSTOMER_CONFIGURATION_NAME = 'Roster_Status_Flag';

    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        LOG_MESSAGE(V_LOG_ID,
                    V_STATUS_WARN,
                    'CustomerFlagStatus not found for customer[' ||
                    VCUSTOMERID ||
                    '] with configuration name- Roster_Status_Flag  ');

      WHEN TOO_MANY_ROWS THEN
        LOG_MESSAGE(V_LOG_ID,
                    V_STATUS_ERROR,
                    'Too many CustomerFlagStatus found for customer [' ||
                    VCUSTOMERID ||
                    '] with configuration name- Roster_Status_Flag .');
        RAISE_APPLICATION_ERROR(-20000,
                                'Too many CustomerFlagStatus found for customer [' ||
                                VCUSTOMERID ||
                                '] with configuration name- Roster_Status_Flag .');
      WHEN OTHERS THEN
        LOG_MESSAGE(V_LOG_ID,
                    V_STATUS_ERROR,
                    'Exception occurred while retrieving CustomerFlagStatus of customer [' ||
                    VCUSTOMERID || '].');

        RAISE;
    END;
    --------------------------------------------------------------
    -----------------    PROCESS STARTING  -----------------------
    --------------------------------------------------------------

    ---------- FOR LOOP 1   ------
    V_IS_CURSOR_RETURNS_VALUE := FALSE;
    FOR RECSCHOOLNODE IN CRSGETSCHOOLLVLNODEFORCUSTOMER(VCUSTOMERID) LOOP

      /*DBMS_OUTPUT.PUT_LINE('School :' || RECSCHOOLNODE.ORG_NODE_ID);*/
      -- to get detail of user associated at top org node id  for created_by
      V_IS_CURSOR_RETURNS_VALUE := TRUE;
      VCREATEDBY                := NULL;
      IF RECSCHOOLNODE.ORG_NODE_ID IS NOT NULL THEN
        BEGIN

          OPEN CRSGETADMINUSER(RECSCHOOLNODE.ORG_NODE_ID);

          FETCH CRSGETADMINUSER
            INTO VCREATEDBY, VROLEID;

          IF CRSGETADMINUSER%NOTFOUND THEN

            OPEN CRSGETCOORDUSER(RECSCHOOLNODE.ORG_NODE_ID);
            FETCH CRSGETCOORDUSER
              INTO VCREATEDBY, VROLEID;

            IF CRSGETCOORDUSER%NOTFOUND THEN

              RAISE NO_DATA_FOUND;

            END IF;

            CLOSE CRSGETCOORDUSER;
          END IF;

          CLOSE CRSGETADMINUSER;

        EXCEPTION
          -- OTHER THAN NO_DATA_FOUND EXCEPTION IS THROWN
          WHEN NO_DATA_FOUND THEN
            LOG_MESSAGE(V_LOG_ID,
                        V_STATUS_WARN,
                        'No Administrator is found for Org id [' ||
                        RECSCHOOLNODE.ORG_NODE_ID || '].');
            IF CRSGETCOORDUSER%ISOPEN THEN
              CLOSE CRSGETCOORDUSER;
            END IF;

            IF CRSGETADMINUSER%ISOPEN THEN
              CLOSE CRSGETADMINUSER;
            END IF;

          WHEN OTHERS THEN
            LOG_MESSAGE(V_LOG_ID,
                        V_STATUS_ERROR,
                        'Exception occurred while retrieving Administrator information for Org id [' ||
                        RECSCHOOLNODE.ORG_NODE_ID || '].');

            IF CRSGETCOORDUSER%ISOPEN THEN
              CLOSE CRSGETCOORDUSER;
            END IF;

            IF CRSGETADMINUSER%ISOPEN THEN
              CLOSE CRSGETADMINUSER;
            END IF;

            RAISE;
        END;
      END IF;

      IF (VCREATEDBY IS NOT NULL) THEN

        /* DBMS_OUTPUT.PUT_LINE('VCREATEDBY : ' || VCREATEDBY);*/
        -------------------------------------------------------
        -- Retrieving productname for testadmin name
        -------------------------------------------------------
        /*BEGIN
          SELECT PRO.PRODUCT_NAME, PRO.SHOW_STUDENT_FEEDBACK
            INTO VPRODUCTNAME, VSHOWSTUDENTFEEDBACK
            FROM PRODUCT PRO
           WHERE PRO.PRODUCT_ID = VPRODUCTID;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            LOG_MESSAGE(V_LOG_ID,
                        V_STATUS_ERROR,
                        'Product id [' || VPRODUCTID ||
                        '] does not exists.');
            RAISE_APPLICATION_ERROR(-20000,
                                    'Product [' || VPRODUCTID ||
                                    '] does not exists.');
          WHEN OTHERS THEN
            LOG_MESSAGE(V_LOG_ID,
                        V_STATUS_ERROR,
                        'Exception occurred while retrieving product.id [' ||
                        VPRODUCTID || ']');
            RAISE;
        END;*/

        IF (RECSCHOOLNODE.ORG_NODE_CODE IS NOT NULL) THEN
          BEGIN
            SELECT ORG.ORG_NODE_CODE
              INTO V_CORPORATE_CODE
              FROM ORG_NODE_PARENT ONP, ORG_NODE ORG
             WHERE ORG.ORG_NODE_ID = ONP.PARENT_ORG_NODE_ID
               AND ONP.ORG_NODE_ID = RECSCHOOLNODE.ORG_NODE_ID;

          EXCEPTION
            WHEN NO_DATA_FOUND THEN
              INCLUDED_CODE := FALSE;
          END;
        ELSE
          INCLUDED_CODE := FALSE;
        END IF;
        /*DBMS_OUTPUT.PUT_LINE('V_CORPORATE_CODE :' || V_CORPORATE_CODE);*/
        --------- FOR LOOP 1.1 ----------------

        V_IS_CURSOR_RETURNS_VALUE := FALSE;

        FOR RECTCITEMSETID IN CRSGETTCTESTFORPRODUCT(VPRODUCTID, VCREATEDBY, INTESTCATALOGID) LOOP
          V_IS_CURSOR_RETURNS_VALUE := TRUE;

          INCLUDED_CODE := FALSE;
          /*DBMS_OUTPUT.PUT_LINE('RECTCITEMSETID.ITEM_SET_ID : ' ||
          RECTCITEMSETID.ITEM_SET_ID);*/

          /*IF (V_CORPORATE_CODE IS NOT NULL) THEN

            SELECT DECODE(COUNT(*), 0, 1, 0)
              INTO NODE_INCLUDED
              FROM INCLUDE_TEST_SCHE_NODE IXSN
             WHERE IXSN.CUSTOMER_ID = VCUSTOMERID
               AND IXSN.CORPORATE_ID = V_CORPORATE_CODE
               AND IXSN.SCHOOL_ID = RECSCHOOLNODE.ORG_NODE_CODE
               AND IXSN.GRADE = RECTCITEMSETID.GRADE;
            --AND IXSN.FORM_ASSIGNMENT = RECTCITEMSETID.ITEM_SET_ID;

            IF (NODE_INCLUDED = 0) THEN
              INCLUDED_CODE := TRUE;
            END IF;
          END IF;*/

          --IF (INCLUDED_CODE) THEN

          /* DBMS_OUTPUT.PUT_LINE('Inside Included');*/
          /* BEGIN
            SELECT DISTINCT CAT.TEST_CATALOG_ID
              INTO VTESTCATALOGID
              FROM TEST_CATALOG CAT
             WHERE ACTIVATION_STATUS = 'AC'
               AND ITEM_SET_ID = RECTCITEMSETID.ITEM_SET_ID;
          EXCEPTION
            WHEN NO_DATA_FOUND THEN
              LOG_MESSAGE(V_LOG_ID,
                          V_STATUS_ERROR,
                          'Catalog does not exists with item set id [' ||
                          RECTCITEMSETID.ITEM_SET_ID || '].');
              RAISE_APPLICATION_ERROR(-20000,
                                      'Catalog does not exists with item set id [' ||
                                      RECTCITEMSETID.ITEM_SET_ID || '].');
            WHEN TOO_MANY_ROWS THEN
              LOG_MESSAGE(V_LOG_ID,
                          V_STATUS_ERROR,
                          'Too many catalog associated with item set id [' ||
                          RECTCITEMSETID.ITEM_SET_ID || '].');
              RAISE_APPLICATION_ERROR(-20000,
                                      'Too many catalog associated with item set id [' ||
                                      RECTCITEMSETID.ITEM_SET_ID || '].');
            WHEN OTHERS THEN
              LOG_MESSAGE(V_LOG_ID,
                          V_STATUS_ERROR,
                          'Exception occurred while retrieving catalog id [' ||
                          RECTCITEMSETID.ITEM_SET_ID || '].');

              RAISE;
          END;*/

          IF (TRIM(RECTCITEMSETID.GRADE) = '6-8') THEN

            /*DBMS_OUTPUT.PUT_LINE('Inside grade 6-8');*/
            ----------
            FOR I IN V_ARRAY6_8.FIRST .. V_ARRAY6_8.LAST LOOP

              IF (V_CORPORATE_CODE IS NOT NULL) THEN

                SELECT DECODE(COUNT(*), 0, 1, 0)
                  INTO NODE_INCLUDED
                  FROM INCLUDE_TEST_SCHE_NODE IXSN
                 WHERE IXSN.CUSTOMER_ID = VCUSTOMERID
                   AND IXSN.CORPORATE_ID = V_CORPORATE_CODE
                   AND IXSN.SCHOOL_ID = RECSCHOOLNODE.ORG_NODE_CODE
                   AND IXSN.GRADE = V_ARRAY6_8(I);
                --AND IXSN.FORM_ASSIGNMENT = RECTCITEMSETID.ITEM_SET_ID;

                IF (NODE_INCLUDED = 0) THEN
                  INCLUDED_CODE := TRUE;
                END IF;
              END IF;

              IF (INCLUDED_CODE) THEN

                BEGIN
                  SELECT DISTINCT CAT.TEST_CATALOG_ID
                    INTO VTESTCATALOGID
                    FROM TEST_CATALOG CAT
                   WHERE ACTIVATION_STATUS = 'AC'
                     AND ITEM_SET_ID = RECTCITEMSETID.ITEM_SET_ID;
                EXCEPTION
                  WHEN NO_DATA_FOUND THEN
                    LOG_MESSAGE(V_LOG_ID,
                                V_STATUS_ERROR,
                                'Catalog does not exists with item set id [' ||
                                RECTCITEMSETID.ITEM_SET_ID || '].');
                    RAISE_APPLICATION_ERROR(-20000,
                                            'Catalog does not exists with item set id [' ||
                                            RECTCITEMSETID.ITEM_SET_ID || '].');
                  WHEN TOO_MANY_ROWS THEN
                    LOG_MESSAGE(V_LOG_ID,
                                V_STATUS_ERROR,
                                'Too many catalog associated with item set id [' ||
                                RECTCITEMSETID.ITEM_SET_ID || '].');
                    RAISE_APPLICATION_ERROR(-20000,
                                            'Too many catalog associated with item set id [' ||
                                            RECTCITEMSETID.ITEM_SET_ID || '].');
                  WHEN OTHERS THEN
                    LOG_MESSAGE(V_LOG_ID,
                                V_STATUS_ERROR,
                                'Exception occurred while retrieving catalog id [' ||
                                RECTCITEMSETID.ITEM_SET_ID || '].');

                    RAISE;
                END;

                OPEN CRSGETSTUFORALLGRADEATSCHOOL(V_ARRAY6_8(I),
                                                  RECSCHOOLNODE.ORG_NODE_ID,
                                                  VPRODUCTID, INTESTCATALOGID);
                FETCH CRSGETSTUFORALLGRADEATSCHOOL
                  INTO V_STUDENT;
                IF (CRSGETSTUFORALLGRADEATSCHOOL%FOUND) THEN

                  CLOSE CRSGETSTUFORALLGRADEATSCHOOL;

                  VTESTAMINACCESSCODE := GENERATE_TAC(V_GENERATED_ACCESS_CODE_LENGTH,
                                                      1);

                  IF SCHEDULINGTYPE = 'roundrobin' THEN

                    SELECT SEQ_TEST_ADMIN_ID.NEXTVAL
                      INTO VTESTADMINID
                      FROM DUAL;

                    INSERT_TEST_ADMIN(VPRODUCTID,
                                      VCUSTOMERID,
                                      RECTCITEMSETID,
                                      VCONFIGDATA,
                                      VSHOWSTUDENTFEEDBACK,
                                      VTESTCATALOGID,
                                      VCREATEDBY,
                                      VTESTAMINACCESSCODE,
                                      RECSCHOOLNODE.ORG_NODE_ID,
                                      VTESTADMINID);

                    V_REC_COUNT := 0;

                    INSERT_ALL_STU_AT_SCHOOL_RR(VPRODUCTID,
                                                VCUSTOMERID,
                                                VCREATEDBY,
                                                VTESTADMINID,
                                                V_ARRAY6_8(I),
                                                RECSCHOOLNODE.ORG_NODE_ID,
                                                VSTUDENTLIST,
                                                VCUSTOMERFLAGSTATUS,
                                                INTESTCATALOGID);

                    INSERT_SCHEDULABLE_UNITS_RR(VCUSTOMERID,
                                                RECTCITEMSETID,
                                                RECSCHOOLNODE.ORG_NODE_NAME,
                                                VTESTADMINID,
                                                VTESTAMINACCESSCODE,
                                                VSTUDENTLIST);

                  ELSIF SCHEDULINGTYPE = 'fixed' THEN
                    SELECT FORM_ASSIGNMENT
                      INTO V_ITEM_SET_ID
                      FROM INCLUDE_TEST_SCHE_NODE IXSN
                     WHERE IXSN.CUSTOMER_ID = VCUSTOMERID
                       AND IXSN.CORPORATE_ID = V_CORPORATE_CODE
                       AND IXSN.SCHOOL_ID = RECSCHOOLNODE.ORG_NODE_CODE
                       AND IXSN.GRADE = V_ARRAY6_8(I);

                    IF RECTCITEMSETID.ITEM_SET_ID = V_ITEM_SET_ID THEN

                      INSERT_TEST_ADMIN(VPRODUCTID,
                                        VCUSTOMERID,
                                        RECTCITEMSETID,
                                        VCONFIGDATA,
                                        VSHOWSTUDENTFEEDBACK,
                                        VTESTCATALOGID,
                                        VCREATEDBY,
                                        VTESTAMINACCESSCODE,
                                        RECSCHOOLNODE.ORG_NODE_ID,
                                        VTESTADMINID);

                      V_REC_COUNT := 0;

                      INSERT_ALL_STU_AT_SCHOOL_FIXED(VPRODUCTID,
                                                     VCUSTOMERID,
                                                     V_FORM_ASSIGNMENT,
                                                     VCREATEDBY,
                                                     VTESTADMINID,
                                                     RECTCITEMSETID.GRADE,
                                                     RECSCHOOLNODE.ORG_NODE_ID,
                                                     VSTUDENTLIST,
                                                     VCUSTOMERFLAGSTATUS,
                                                     INTESTCATALOGID);

                      INSERT_SCHEDULABLE_UNITS(VCUSTOMERID,
                                               RECTCITEMSETID,
                                               RECSCHOOLNODE.ORG_NODE_NAME,
                                               VTESTADMINID,
                                               VTESTAMINACCESSCODE,
                                               VSTUDENTLIST);
                    END IF;
                  ELSE
                    INSERT_TEST_ADMIN(VPRODUCTID,
                                      VCUSTOMERID,
                                      RECTCITEMSETID,
                                      VCONFIGDATA,
                                      VSHOWSTUDENTFEEDBACK,
                                      VTESTCATALOGID,
                                      VCREATEDBY,
                                      VTESTAMINACCESSCODE,
                                      RECSCHOOLNODE.ORG_NODE_ID,
                                      VTESTADMINID);

                    V_REC_COUNT := 0;

                    INSERT_ALL_STU_AT_SCHOOL(VPRODUCTID,
                                             VCUSTOMERID,
                                             VCREATEDBY,
                                             VTESTADMINID,
                                             V_ARRAY6_8(I),
                                             RECSCHOOLNODE.ORG_NODE_ID,
                                             VSTUDENTLIST,
                                             VCUSTOMERFLAGSTATUS,
                                             INTESTCATALOGID);

                    INSERT_SCHEDULABLE_UNITS(VCUSTOMERID,
                                             RECTCITEMSETID,
                                             RECSCHOOLNODE.ORG_NODE_NAME,
                                             VTESTADMINID,
                                             VTESTAMINACCESSCODE,
                                             VSTUDENTLIST);
                  END IF;
                  ---------- FOR LOOP 1.1.1  ------
                  /*
                      Insert the itemset record associated with test(TC) of test session
                  */
                  VSTUDENTLIST := '';

                ELSE
                  CLOSE CRSGETSTUFORALLGRADEATSCHOOL;
                END IF;
              END IF;
            END LOOP;

          ELSE

            IF (V_CORPORATE_CODE IS NOT NULL) THEN
            INCLUDED_CODE := FALSE; --Changed for Prescheduling story 1549
              SELECT DECODE(COUNT(*), 0, 1, 0)
                INTO NODE_INCLUDED
                FROM INCLUDE_TEST_SCHE_NODE IXSN
               WHERE IXSN.CUSTOMER_ID = VCUSTOMERID
                 AND IXSN.CORPORATE_ID = V_CORPORATE_CODE
                 AND IXSN.SCHOOL_ID = RECSCHOOLNODE.ORG_NODE_CODE
                 AND IXSN.GRADE = RECTCITEMSETID.GRADE
                 AND IXSN.FORM_ASSIGNMENT = RECTCITEMSETID.ITEM_SET_ID;

              IF (NODE_INCLUDED = 0) THEN
                INCLUDED_CODE := TRUE;
              END IF;
            END IF;

            IF (INCLUDED_CODE) THEN

              BEGIN
                SELECT DISTINCT CAT.TEST_CATALOG_ID
                  INTO VTESTCATALOGID
                  FROM TEST_CATALOG CAT
                 WHERE ACTIVATION_STATUS = 'AC'
                   AND ITEM_SET_ID = RECTCITEMSETID.ITEM_SET_ID;
              EXCEPTION
                WHEN NO_DATA_FOUND THEN
                  LOG_MESSAGE(V_LOG_ID,
                              V_STATUS_ERROR,
                              'Catalog does not exists with item set id [' ||
                              RECTCITEMSETID.ITEM_SET_ID || '].');
                  RAISE_APPLICATION_ERROR(-20000,
                                          'Catalog does not exists with item set id [' ||
                                          RECTCITEMSETID.ITEM_SET_ID || '].');
                WHEN TOO_MANY_ROWS THEN
                  LOG_MESSAGE(V_LOG_ID,
                              V_STATUS_ERROR,
                              'Too many catalog associated with item set id [' ||
                              RECTCITEMSETID.ITEM_SET_ID || '].');
                  RAISE_APPLICATION_ERROR(-20000,
                                          'Too many catalog associated with item set id [' ||
                                          RECTCITEMSETID.ITEM_SET_ID || '].');
                WHEN OTHERS THEN
                  LOG_MESSAGE(V_LOG_ID,
                              V_STATUS_ERROR,
                              'Exception occurred while retrieving catalog id [' ||
                              RECTCITEMSETID.ITEM_SET_ID || '].');

                  RAISE;
              END;
              OPEN CRSGETSTUFORALLGRADEATSCHOOL(RECTCITEMSETID.GRADE,
                                                RECSCHOOLNODE.ORG_NODE_ID,
                                                VPRODUCTID, INTESTCATALOGID);
              FETCH CRSGETSTUFORALLGRADEATSCHOOL
                INTO V_STUDENT;
              IF (CRSGETSTUFORALLGRADEATSCHOOL%FOUND) THEN

                CLOSE CRSGETSTUFORALLGRADEATSCHOOL;

                VTESTAMINACCESSCODE := GENERATE_TAC(V_GENERATED_ACCESS_CODE_LENGTH,
                                                    1);
                /*DBMS_OUTPUT.PUT_LINE('found students');*/
                SELECT SEQ_TEST_ADMIN_ID.NEXTVAL
                  INTO VTESTADMINID
                  FROM DUAL;

                IF SCHEDULINGTYPE = 'roundrobin' THEN

                  INSERT_TEST_ADMIN(VPRODUCTID,
                                    VCUSTOMERID,
                                    RECTCITEMSETID,
                                    VCONFIGDATA,
                                    VSHOWSTUDENTFEEDBACK,
                                    VTESTCATALOGID,
                                    VCREATEDBY,
                                    VTESTAMINACCESSCODE,
                                    RECSCHOOLNODE.ORG_NODE_ID,
                                    VTESTADMINID);

                  V_REC_COUNT := 0;

                  INSERT_ALL_STU_AT_SCHOOL_RR(VPRODUCTID,
                                              VCUSTOMERID,
                                              VCREATEDBY,
                                              VTESTADMINID,
                                              RECTCITEMSETID.GRADE,
                                              RECSCHOOLNODE.ORG_NODE_ID,
                                              VSTUDENTLIST,
                                              VCUSTOMERFLAGSTATUS,
                                              INTESTCATALOGID);

                  INSERT_SCHEDULABLE_UNITS_RR(VCUSTOMERID,
                                              RECTCITEMSETID,
                                              RECSCHOOLNODE.ORG_NODE_NAME,
                                              VTESTADMINID,
                                              VTESTAMINACCESSCODE,
                                              VSTUDENTLIST);

                ELSIF SCHEDULINGTYPE = 'fixed' THEN
                  SELECT FORM_ASSIGNMENT
                    INTO V_ITEM_SET_ID
                    FROM INCLUDE_TEST_SCHE_NODE IXSN
                   WHERE IXSN.CUSTOMER_ID = VCUSTOMERID
                     AND IXSN.CORPORATE_ID = V_CORPORATE_CODE
                     AND IXSN.SCHOOL_ID = RECSCHOOLNODE.ORG_NODE_CODE
                     AND IXSN.GRADE = RECTCITEMSETID.GRADE
                     AND IXSN.FORM_ASSIGNMENT = RECTCITEMSETID.ITEM_SET_ID;

                  IF RECTCITEMSETID.ITEM_SET_ID = V_ITEM_SET_ID THEN

                    INSERT_TEST_ADMIN(VPRODUCTID,
                                      VCUSTOMERID,
                                      RECTCITEMSETID,
                                      VCONFIGDATA,
                                      VSHOWSTUDENTFEEDBACK,
                                      VTESTCATALOGID,
                                      VCREATEDBY,
                                      VTESTAMINACCESSCODE,
                                      RECSCHOOLNODE.ORG_NODE_ID,
                                      VTESTADMINID);

                    V_REC_COUNT := 0;
                    INSERT_ALL_STU_AT_SCHOOL_FIXED(VPRODUCTID,
                                                   VCUSTOMERID,
                                                   V_FORM_ASSIGNMENT,
                                                   VCREATEDBY,
                                                   VTESTADMINID,
                                                   RECTCITEMSETID.GRADE,
                                                   RECSCHOOLNODE.ORG_NODE_ID,
                                                   VSTUDENTLIST,
                                                   VCUSTOMERFLAGSTATUS,
                                                   INTESTCATALOGID);
                    INSERT_SCHEDULABLE_UNITS(VCUSTOMERID,
                                             RECTCITEMSETID,
                                             RECSCHOOLNODE.ORG_NODE_NAME,
                                             VTESTADMINID,
                                             VTESTAMINACCESSCODE,
                                             VSTUDENTLIST);
                  END IF;
                ELSE

                  INSERT_TEST_ADMIN(VPRODUCTID,
                                    VCUSTOMERID,
                                    RECTCITEMSETID,
                                    VCONFIGDATA,
                                    VSHOWSTUDENTFEEDBACK,
                                    VTESTCATALOGID,
                                    VCREATEDBY,
                                    VTESTAMINACCESSCODE,
                                    RECSCHOOLNODE.ORG_NODE_ID,
                                    VTESTADMINID);

                  V_REC_COUNT := 0;
                  INSERT_ALL_STU_AT_SCHOOL(VPRODUCTID,
                                           VCUSTOMERID,
                                           VCREATEDBY,
                                           VTESTADMINID,
                                           RECTCITEMSETID.GRADE,
                                           RECSCHOOLNODE.ORG_NODE_ID,
                                           VSTUDENTLIST,
                                           VCUSTOMERFLAGSTATUS,
                                           INTESTCATALOGID);

                  INSERT_SCHEDULABLE_UNITS(VCUSTOMERID,
                                           RECTCITEMSETID,
                                           RECSCHOOLNODE.ORG_NODE_NAME,
                                           VTESTADMINID,
                                           VTESTAMINACCESSCODE,
                                           VSTUDENTLIST);
                END IF;
                ---------- FOR LOOP 1.1.1  ------
                /*
                    Insert the itemset record associated with test(TC) of test session
                */
                VSTUDENTLIST := '';

              ELSE
                CLOSE CRSGETSTUFORALLGRADEATSCHOOL;
              END IF;

            END IF;
          END IF;
          --END IF;
        END LOOP;

        /*  IF (NOT V_IS_CURSOR_RETURNS_VALUE) THEN
          -- LOGGING ONLY, WHEN CURSOR "crsGetTCTestForProduct" DOES NOT RETURNS ANY VALUE
          LOG_MESSAGE(V_LOG_ID,
                      V_STATUS_ERROR,
                      'No valid catalog found for product id [' ||
                      VPRODUCTID || '] and user id [' || VCREATEDBY || '].');
          -- IF YOU DO NOT WISH THROW ERROR THEN SET TO  TRUE "V_IS_CURSOR_RETURNS_VALUE" AS PREVIOUS LOOP IS EXECUTING
          RAISE_APPLICATION_ERROR(-20000,
                                  'No valid catalog found for product id [' ||
                                  VPRODUCTID || '] and user id [' ||
                                  VCREATEDBY || '].');
        END IF;*/

        COMMIT; -- need to remove comment before QA delivery
      END IF;
    END LOOP;
    IF (NOT V_IS_CURSOR_RETURNS_VALUE) THEN
      -- LOGGING ONLY, WHEN CURSOR "crsGetSchoollvlNodeForCustomer" DOES NOT RETURNS ANY VALUE
      LOG_MESSAGE(V_LOG_ID,
                  V_STATUS_ERROR,
                  'No valid school level org_node_id is found for Customer id [' ||
                  VCUSTOMERID || '].');
      RAISE_APPLICATION_ERROR(-20000,
                              'No valid school level org_node_id is found for Customer id [' ||
                              VCUSTOMERID || '].');
    END IF;

    LOG_MESSAGE(V_LOG_ID,
                V_STATUS_COMPLETE,
                'Procedure completed successfully.');
  EXCEPTION
    WHEN OTHERS THEN
      LOG_MESSAGE(V_LOG_ID,
                  V_STATUS_COMPLETE,
                  'Procedure completed with error:' ||
                  DBMS_UTILITY.FORMAT_ERROR_BACKTRACE);
      ROLLBACK; -- ROLLBACK ALL DATA MODIFIED EXPLICITELY
      RAISE;
  END PROCESS_IN_CREATE_TEST_SESSION;

END IREAD3_PRESCHEDULING_2015_SUMR;
/
