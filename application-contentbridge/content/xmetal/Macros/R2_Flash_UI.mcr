<?xml version="1.0"?>਍㰀℀䐀伀䌀吀夀倀䔀 䴀䄀䌀刀伀匀 匀夀匀吀䔀䴀 ∀洀愀挀爀漀猀⸀搀琀搀∀㸀ഀഀ
<MACROS>਍ഀഀ
<MACRO name="TeamXML Workflow" hide="true" lang="JScript" id="2000">਍㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
// DESCRIPTION:਍⼀⼀    吀栀椀猀 洀愀挀爀漀 挀爀攀愀琀攀 琀栀攀 吀攀愀洀堀䴀䰀 圀漀爀欀昀氀漀眀 椀渀 刀攀猀漀甀爀挀攀 䴀愀渀愀最攀爀⸀ഀഀ
// HISTORY:਍⼀⼀ ㄀⸀ ㈀　　㈀　㤀㄀㠀㨀 䌀漀爀攀氀㨀 䐀愀瘀椀搀 一最漀ഀഀ
//    Initial Release.਍昀甀渀挀琀椀漀渀 挀爀攀愀琀攀圀漀爀欀昀氀漀眀 ⠀⤀ 笀ഀഀ
   try {਍      瘀愀爀 䈀爀漀眀猀攀爀㴀刀攀猀漀甀爀挀攀䴀愀渀愀最攀爀⸀䄀猀猀攀琀猀⸀圀攀戀䈀爀漀眀猀攀爀㬀 ഀഀ
      //Browser.Navigate2("http://atlantis.eppg.com:82/iw-bin/select_branch.cgi",0,"TeamXML Workflow");਍      椀昀 ⠀䄀瀀瀀氀椀挀愀琀椀漀渀⸀䌀甀猀琀漀洀倀爀漀瀀攀爀琀椀攀猀⸀椀琀攀洀⠀∀圀漀爀欀䘀氀漀眀唀刀䰀∀⤀ ℀㴀 渀甀氀氀⤀ 笀ഀഀ
         Browser.Navigate2(Application.CustomProperties.item("WorkFlowURL").Value,0,"TeamXML Workflow");਍      紀 攀氀猀攀 笀ഀഀ
         Application.Alert ("WorkFlow-URL in the (xmcus.ini) file is not avaiable.");਍      紀ഀഀ
   } catch (e) {਍   紀ഀഀ
}਍挀爀攀愀琀攀圀漀爀欀昀氀漀眀⠀⤀㬀ഀഀ
]]>਍㰀⼀䴀䄀䌀刀伀㸀ഀഀ
<MACRO name="Preview Document" hide="true" key="Ctrl+M" lang="JScript">਍㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
function PreviewDocument () {਍   椀昀 ⠀䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀嘀椀攀眀吀礀瀀攀 㴀㴀 猀焀嘀椀攀眀䈀爀漀眀猀攀⤀ 笀ഀഀ
      Application.Run ("On_Before_Document_Preview");਍   紀 攀氀猀攀 笀ഀഀ
      ActiveDocument.ViewType = sqViewBrowse;਍   紀ഀഀ
}਍倀爀攀瘀椀攀眀䐀漀挀甀洀攀渀琀⠀⤀㬀ഀഀ
]]>਍㰀⼀䴀䄀䌀刀伀㸀ഀഀ
਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀䴀漀瘀攀 伀甀琀 伀渀攀 䰀攀瘀攀氀∀ 栀椀搀攀㴀∀琀爀甀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㄀㈀㌀㄀∀㸀ഀഀ
<![CDATA[਍ഀഀ
function MoveToContainerNode () {਍   椀昀 ⠀⠀匀攀氀攀挀琀椀漀渀⸀䌀漀渀琀愀椀渀攀爀一漀搀攀⤀ ☀☀ ⠀匀攀氀攀挀琀椀漀渀⸀䌀漀渀琀愀椀渀攀爀一漀搀攀⸀渀漀搀攀一愀洀攀 ℀㴀 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀搀漀挀甀洀攀渀琀䔀氀攀洀攀渀琀⸀渀漀搀攀一愀洀攀⤀⤀ 笀ഀഀ
      Selection.SelectAfterNode(Selection.ContainerNode);਍   紀ഀഀ
}਍䴀漀瘀攀吀漀䌀漀渀琀愀椀渀攀爀一漀搀攀⠀⤀㬀ഀഀ
]]>਍㰀⼀䴀䄀䌀刀伀㸀ഀഀ
਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀伀渀开嘀椀攀眀开䌀栀愀渀最攀∀ 栀椀搀攀㴀∀琀爀甀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀㸀ഀഀ
<![CDATA[਍ഀഀ
function OnViewChange () {਍   椀昀 ⠀䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀嘀椀攀眀吀礀瀀攀㴀㴀㌀⤀ 笀ഀഀ
      if (ActiveDocument.CustomDocumentProperties.item("SwitchView") != null) {਍         䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀嘀椀攀眀吀礀瀀攀㴀 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀倀爀攀瘀椀漀甀猀嘀椀攀眀吀礀瀀攀㬀ഀഀ
         ActiveDocument.CustomDocumentProperties.item("SwitchView").Delete();਍      紀ഀഀ
   }਍   ⼀⼀ 匀攀琀 倀䤀 琀漀 爀攀愀搀漀渀氀礀 猀漀 琀栀愀琀 椀琀 眀漀渀✀琀 猀栀漀眀 甀瀀 椀渀 一漀爀洀愀氀 愀渀搀 吀愀猀伀渀⸀ഀഀ
   if ((ActiveDocument.ViewType==0 || ActiveDocument.ViewType==1) && ActiveDocument.PreviousViewType==2) {਍      瘀愀爀 爀漀漀琀㴀䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀搀漀挀甀洀攀渀琀䔀氀攀洀攀渀琀㬀ഀഀ
      if (root) ProcessPermissions(ActiveDocument, root);਍   紀ഀഀ
   // Reset current Selection if it has changed before switching over to normal view.਍   椀昀 ⠀最氀漀戀愀氀刀愀渀最攀 ℀㴀 渀甀氀氀⤀ 笀ഀഀ
      if (ActiveDocument.ViewType == sqViewNormal) {਍         瘀愀爀 爀渀最 㴀 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀刀愀渀最攀㬀ഀഀ
         if (globalRange.ContainerNode != null) {਍            椀昀 ⠀爀渀最⸀䌀漀渀琀愀椀渀攀爀一漀搀攀⸀渀漀搀攀一愀洀攀 ℀㴀 最氀漀戀愀氀刀愀渀最攀⸀䌀漀渀琀愀椀渀攀爀一漀搀攀⸀渀漀搀攀一愀洀攀⤀ 笀ഀഀ
               globalRange.Select();਍            紀ഀഀ
         }਍      紀ഀഀ
   }਍紀ഀഀ
OnViewChange();਍ഀഀ
]]>਍㰀⼀䴀䄀䌀刀伀㸀ഀഀ
਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀伀渀开唀瀀搀愀琀攀开唀䤀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 栀椀搀攀㴀∀琀爀甀攀∀ 椀搀㴀∀㄀㐀㐀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
// #######################################################################਍⼀⼀  匀琀愀爀琀椀渀最  䌀漀搀攀⸀ ⴀഀഀ
// ####################################################################### ਍⼀⼀ 䐀椀猀愀戀氀攀 洀漀猀琀 洀愀挀爀漀猀 椀昀 椀渀 倀氀愀椀渀 吀攀砀琀 瘀椀攀眀 漀爀 椀昀 琀栀攀 搀漀挀甀洀攀渀琀 椀猀 渀漀琀 堀䴀䰀ഀഀ
// ######################################################################਍ഀഀ
function OnUpdateUI () {਍   椀昀 ⠀℀䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀䤀猀堀䴀䰀 簀簀ഀഀ
      (ActiveDocument.ViewType != sqViewNormal && ActiveDocument.ViewType != sqViewTagsOn)) {਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀䤀渀猀攀爀琀 一攀眀 䰀椀猀琀 䤀琀攀洀∀⤀㬀ഀഀ
      Application.DisableMacro ("Insert Comment");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀䤀渀猀攀爀琀 匀琀甀搀攀渀琀 䐀椀爀攀挀琀椀漀渀猀∀⤀㬀ഀഀ
      Application.DisableMacro ("Insert Stimulus");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀䤀渀猀攀爀琀 䰀攀愀搀䤀渀∀⤀㬀ഀഀ
      Application.DisableMacro ("Insert Distractor Rationale");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀吀漀最最氀攀 䌀漀洀洀攀渀琀猀∀⤀㬀ഀഀ
      Application.DisableMacro ("Expand All Comments");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀䌀漀氀氀愀瀀猀攀 䄀氀氀 䌀漀洀洀攀渀琀猀∀⤀㬀ഀഀ
      Application.DisableMacro ("Toggle Hierarchy");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀䔀砀瀀愀渀搀 䄀氀氀 䠀椀攀爀愀爀挀栀礀∀⤀㬀ഀഀ
      Application.DisableMacro ("Collapse All Hierarchy");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀䴀漀瘀攀 伀甀琀 伀渀攀 䰀攀瘀攀氀∀⤀㬀ഀഀ
      Application.DisableMacro ("Insert BoxDirection");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀䤀渀猀攀爀琀 䰀椀渀攀猀∀⤀㬀ഀഀ
      Application.DisableMacro ("Insert TextBox");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀䤀渀猀攀爀琀 䈀漀砀䐀椀爀攀挀琀椀漀渀∀⤀㬀ഀഀ
      Application.DisableMacro ("Insert BoxAnswer");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀䤀渀猀攀爀琀 唀匀瀀愀挀攀∀⤀㬀ഀഀ
      Application.DisableMacro ("Insert Page Break");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开䴀搀愀猀栀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Ndash");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开倀氀甀猀 漀爀 洀椀渀甀猀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Addition");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开䐀椀瘀椀猀椀漀渀 猀礀洀戀漀氀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Multiplication");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开䴀甀氀琀椀瀀氀椀挀愀琀椀漀渀⼀䈀甀氀氀攀爀⼀倀漀椀渀琀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Equals");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开一漀琀 攀焀甀愀氀 琀漀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Less than");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开䜀爀攀愀琀攀爀 琀栀愀渀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Less than or equal to");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开䜀爀攀愀琀攀爀 琀栀攀渀 漀爀 攀焀甀愀氀 琀漀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Infinity");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开匀焀甀愀爀攀 刀漀漀琀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Open bracket");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开䌀氀漀猀攀 戀爀愀挀欀攀琀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Open brace");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开䌀氀漀猀攀 戀爀愀挀攀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Copyright");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开刀攀最椀猀琀攀爀攀搀 琀爀愀搀攀洀愀爀欀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Trademark");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开匀椀渀最氀攀 漀瀀攀渀 焀甀漀琀攀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Single close quote");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开䄀猀琀攀爀椀猀欀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Ellipses");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开倀爀椀洀攀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Degrees");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开䄀戀猀漀氀甀琀攀 瘀愀氀甀攀 戀愀爀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Perpendicular");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开倀愀爀愀氀氀攀氀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Similar to");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开一漀琀 攀焀甀愀氀 琀漀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Congruent");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开䄀瀀瀀爀漀砀椀洀愀琀攀氀礀 攀焀甀愀氀 琀漀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Segment/repeating decimal");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开䰀椀渀攀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Ray");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开吀爀椀愀渀最氀攀∀⤀㬀ഀഀ
      Application.DisableMacro ("_PI");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开一攀最愀琀椀瘀攀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Empty set");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开匀攀琀 琀栀攀漀爀礀⼀匀甀戀猀攀琀 伀昀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Set theory/Subset Of or Equal To");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开匀攀琀 琀栀攀漀爀礀⼀唀渀椀漀渀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Set theory/Intersection");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开吀栀攀爀攀昀漀爀攀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Nth root");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开䜀爀攀攀欀 氀攀琀琀攀爀⼀匀洀愀氀氀 䰀攀琀琀攀爀 吀栀攀琀愀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Greek letter/Capital Letter Chi");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开䜀爀攀攀欀 氀攀琀琀攀爀⼀一ⴀ䄀爀礀 匀甀洀洀愀琀椀漀渀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Greek letter/Small Letter Alpha");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开匀甀瀀攀爀猀挀爀椀瀀琀 　∀⤀㬀ഀഀ
      Application.DisableMacro ("_Superscript 1");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开匀甀瀀攀爀猀挀爀椀瀀琀 ㈀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Superscript 3");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开匀甀瀀攀爀猀挀爀椀瀀琀 㐀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Superscript 5");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开匀甀瀀攀爀猀挀爀椀瀀琀 㘀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Superscript 7");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开匀甀瀀攀爀猀挀爀椀瀀琀 㠀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Superscript 9");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开匀甀瀀攀爀猀挀爀椀瀀琀 ⬀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Superscript -");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开匀甀瀀攀爀猀挀爀椀瀀琀 㴀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Superscript (");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开匀甀瀀攀爀猀挀爀椀瀀琀 ⤀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Superscript n");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开匀甀戀猀挀爀椀瀀琀 　∀⤀㬀ഀഀ
      Application.DisableMacro ("_Subscript 1");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开匀甀戀猀挀爀椀瀀琀 ㈀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Subscript 3");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开匀甀戀猀挀爀椀瀀琀 㐀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Subscript 5");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开匀甀戀猀挀爀椀瀀琀 㘀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Subscript 7");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开匀甀戀猀挀爀椀瀀琀 㠀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Subscript 9");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开匀甀戀猀挀爀椀瀀琀 ⬀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Subscript -");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开匀甀戀猀挀爀椀瀀琀 㴀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Subscript (");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开匀甀戀猀挀爀椀瀀琀 ⤀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Accented a");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开䄀挀挀攀渀琀攀搀 攀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Accented i");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开䄀挀挀攀渀琀攀搀 漀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Accented u");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开䰀椀最愀琀甀爀攀 昀椀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Thin Space");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开攀洀 匀瀀愀挀攀∀⤀㬀ഀഀ
      Application.DisableMacro ("_en Space");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开昀甀渀挀琀椀漀渀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Circle");਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀开䌀攀渀琀∀⤀㬀ഀഀ
      Application.DisableMacro ("_Angle");਍   紀 攀氀猀攀 椀昀 ⠀䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀嘀椀攀眀吀礀瀀攀 㴀㴀 猀焀嘀椀攀眀一漀爀洀愀氀 簀簀 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀嘀椀攀眀吀礀瀀攀 㴀㴀 猀焀嘀椀攀眀吀愀最猀伀渀⤀ 笀ഀഀ
      if ((! Selection.IsParentElement("OrderedList")) && ਍          ⠀℀ 匀攀氀攀挀琀椀漀渀⸀䤀猀倀愀爀攀渀琀䔀氀攀洀攀渀琀⠀∀唀渀漀爀搀攀爀攀搀䰀椀猀琀∀⤀⤀⤀ 笀ഀഀ
          Application.DisableMacro("Insert New List Item"); ਍      紀ഀഀ
      // Save Tags On Rang location so that we can reset to this location਍      ⼀⼀ 漀渀开瘀椀攀眀开挀栀愀渀最攀⸀ഀഀ
      if (ActiveDocument.ViewType == sqViewTagsOn) {਍         最氀漀戀愀氀刀愀渀最攀 㴀 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀刀愀渀最攀㬀ഀഀ
      }਍      椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀漀渀琀愀椀渀攀爀一漀搀攀⤀ 笀ഀഀ
         if (Selection.ContainerNode.nodeName == ActiveDocument.documentElement.nodeName) {਍            䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀䴀漀瘀攀 伀甀琀 伀渀攀 䰀攀瘀攀氀∀⤀㬀ഀഀ
         }਍      紀 攀氀猀攀 笀ഀഀ
         Application.DisableMacro ("Move Out One Level");਍      紀ഀഀ
      if (! Selection.IsParentElement("Item")) {਍         䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀䤀渀猀攀爀琀 䰀椀渀攀猀∀⤀㬀ഀഀ
         Application.DisableMacro ("Insert TextBox");਍      紀ഀഀ
      if (! Selection.IsParentElement("TextBox")) {਍         䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀䤀渀猀攀爀琀 䈀漀砀䐀椀爀攀挀琀椀漀渀∀⤀㬀ഀഀ
         Application.DisableMacro ("Insert BoxAnswer");਍      紀 攀氀猀攀 笀ഀഀ
          Application.DisableMacro ("Insert Page Break");਍      紀ഀഀ
      if (! Selection.CanInsertText) {਍         䄀瀀瀀氀椀挀愀琀椀漀渀⸀䐀椀猀愀戀氀攀䴀愀挀爀漀 ⠀∀䤀渀猀攀爀琀 唀匀瀀愀挀攀∀⤀㬀ഀഀ
      }਍   紀ഀഀ
  // Prevent user from selecting outside of last Element.਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀漀渀琀愀椀渀攀爀一漀搀攀 㴀㴀 渀甀氀氀⤀ 笀ഀഀ
      Selection.MoveRight();਍   紀ഀഀ
   if (Selection.ContainerNode == null) {਍      匀攀氀攀挀琀椀漀渀⸀䴀漀瘀攀䰀攀昀琀⠀⤀㬀ഀഀ
   }਍紀ഀഀ
਍伀渀唀瀀搀愀琀攀唀䤀⠀⤀㬀ഀഀ
]]>਍㰀⼀䴀䄀䌀刀伀㸀ഀഀ
਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀䰀䤀䈀刀䄀刀夀 ⴀ 刀攀最椀猀琀爀礀 昀甀渀挀琀椀漀渀猀∀ 栀椀搀攀㴀∀琀爀甀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀㸀ഀഀ
<![CDATA[਍昀甀渀挀琀椀漀渀 䬀攀礀䔀砀椀猀琀 ⠀猀琀爀䬀攀礀⤀  笀ഀഀ
  ਍  瘀愀爀 昀刀攀琀甀爀渀 㴀 琀爀甀攀㬀ഀഀ
  var wsh = new ActiveXObject("WScript.Shell") ;਍  琀爀礀笀ഀഀ
     wsh.RegRead(strKey)਍  紀 挀愀琀挀栀⠀攀⤀笀ഀഀ
     var sTest = e.description.substr(0,7);਍     椀昀⠀猀吀攀猀琀㴀㴀∀唀渀愀戀氀攀 ∀⤀笀ഀഀ
        fReturn = true;਍        ⼀⼀眀猀栀⸀倀漀瀀甀瀀⠀∀欀攀礀 攀砀椀猀琀猀 戀甀琀 渀漀 搀攀昀愀甀氀琀 瘀愀氀甀攀 猀攀琀∀⤀ഀഀ
     } else {਍        椀昀⠀猀吀攀猀琀㴀㴀∀䤀渀瘀愀氀椀搀∀⤀笀ഀഀ
           fReturn = false;਍           ⼀⼀眀猀栀⸀倀漀瀀甀瀀⠀∀欀攀礀 搀漀攀猀 渀漀琀 攀砀椀猀琀∀⤀ഀഀ
        } else {਍           昀刀攀琀甀爀渀 㴀 昀愀氀猀攀㬀ഀഀ
           Application.Alert ("Error in KeyExist, Registry Key read error : " + e.description);਍           ⼀⼀眀猀栀⸀倀漀瀀甀瀀⠀攀⸀搀攀猀挀爀椀瀀琀椀漀渀⤀㬀ഀഀ
        }਍     紀ഀഀ
  }਍  眀猀栀 㴀 渀甀氀氀㬀ഀഀ
  return fReturn;਍紀ഀഀ
]]>਍㰀⼀䴀䄀䌀刀伀㸀ഀഀ
਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀䰀䤀䈀刀䄀刀夀 ⴀ 䐀漀挀甀洀攀渀琀 氀攀瘀攀氀 昀甀渀挀琀椀漀渀猀∀ 栀椀搀攀㴀∀琀爀甀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀㸀ഀഀ
<![CDATA[਍昀甀渀挀琀椀漀渀 吀甀爀渀伀渀吀爀愀挀欀䌀栀愀渀最攀猀 ⠀⤀笀ഀഀ
   var cmdBars = Application.CommandBars;਍   瘀愀爀 挀洀搀䈀愀爀 㴀 挀洀搀䈀愀爀猀⸀椀琀攀洀⠀∀刀攀瘀椀攀眀椀渀最∀⤀㬀ഀഀ
   // get the first button਍   瘀愀爀 挀洀搀䈀愀爀䌀漀渀琀爀漀氀 㴀 挀洀搀䈀愀爀⸀䌀漀渀琀爀漀氀猀⸀椀琀攀洀⠀㄀⤀㬀 ഀഀ
   // execute the built-in command ਍   挀洀搀䈀愀爀䌀漀渀琀爀漀氀⸀䔀砀攀挀甀琀攀⠀⤀㬀 ഀഀ
}਍ഀഀ
function getAreaVPathFromLaunchPad () {਍   瘀瀀愀琀栀㴀渀甀氀氀㬀ഀഀ
   var areaPath=null;਍   琀爀礀 笀ഀഀ
      var strFullName = ActiveDocument.FullName;਍      愀爀攀愀倀愀琀栀 㴀 猀琀爀䘀甀氀氀一愀洀攀⸀洀愀琀挀栀 ⠀⼀尀尀挀愀挀栀攀⠀尀尀搀攀昀愀甀氀琀尀尀洀愀椀渀⸀⬀㼀尀尀⠀圀伀刀䬀䄀刀䔀䄀簀䔀䐀䤀吀䤀伀一⤀尀尀嬀帀尀尀崀⬀⤀⼀⤀㬀ഀഀ
      if (areaPath == null) {਍         愀爀攀愀倀愀琀栀 㴀 猀琀爀䘀甀氀氀一愀洀攀⸀洀愀琀挀栀 ⠀⼀尀尀挀愀挀栀攀⠀尀尀搀攀昀愀甀氀琀尀尀洀愀椀渀⸀⬀㼀尀尀匀吀䄀䜀䤀一䜀⤀⼀⤀㬀ഀഀ
      }਍      椀昀 ⠀愀爀攀愀倀愀琀栀 ℀㴀 渀甀氀氀⤀ 笀ഀഀ
         vpath = areaPath[1];਍         瘀瀀愀琀栀 㴀 瘀瀀愀琀栀⸀爀攀瀀氀愀挀攀 ⠀⼀尀尀⼀最Ⰰ ∀⼀∀⤀㬀ഀഀ
      }਍   紀 挀愀琀挀栀 ⠀攀⤀ 笀紀㬀ഀഀ
   return vpath;਍紀ഀഀ
਍崀崀㸀ഀഀ
</MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀伀渀开䐀漀挀甀洀攀渀琀开伀瀀攀渀开嘀椀攀眀∀ 栀椀搀攀㴀∀琀爀甀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀㸀ഀഀ
<![CDATA[਍ഀഀ
var globalRange = null;਍ഀഀ
function setVPath () {਍   琀爀礀 笀ഀഀ
      var rng = ActiveDocument.Range;਍      瘀愀爀 搀漀挀㴀䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀㬀ഀഀ
      var areaPath=GetAttrValue(doc,"interwoven","areaVPath");਍      椀昀 ⠀愀爀攀愀倀愀琀栀 㴀㴀 渀甀氀氀⤀ 笀ഀഀ
         areaPath =  getAreaVPathFromLaunchPad(); // Get VPath from local filesystem.਍         椀昀 ⠀愀爀攀愀倀愀琀栀 ℀㴀 渀甀氀氀⤀ 笀ഀഀ
            if (!PutAttr(doc, "interwoven", "areaVPath", areaPath)) {਍                䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀⠀∀䘀愀椀氀攀搀 琀漀 猀愀瘀攀 愀爀攀愀嘀倀愀琀栀 椀渀 搀漀挀甀洀攀渀琀⸀∀Ⰰ ∀䈀䴀倀 椀洀愀最攀猀 洀椀最栀琀 渀漀琀 戀攀 愀瘀愀椀愀戀氀攀 昀漀爀 爀攀瘀椀攀眀⸀∀⤀㬀ഀഀ
            } else {਍               䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀匀愀瘀攀⠀⤀㬀ഀഀ
               ActiveDocument.Reload();਍               瘀愀爀 爀漀漀琀㴀䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀搀漀挀甀洀攀渀琀䔀氀攀洀攀渀琀㬀ഀഀ
               if (root) ProcessPermissions(ActiveDocument, root);਍            紀ഀഀ
         }਍      紀ഀഀ
   } catch (e) {};਍紀ഀഀ
਍⼀⼀昀甀渀挀琀椀漀渀 伀渀䐀漀挀甀洀攀渀琀伀瀀攀渀嘀椀攀眀 ⠀⤀ 笀ഀഀ
//   TurnOnTrackChanges(); ਍⼀⼀紀ഀഀ
//OnDocumentOpenView ();਍ഀഀ
setVPath ();਍崀崀㸀ഀഀ
</MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀伀渀开䐀漀挀甀洀攀渀琀开伀瀀攀渀开䌀漀洀瀀氀攀琀攀∀ 栀椀搀攀㴀∀琀爀甀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀㸀ഀഀ
<![CDATA[਍昀甀渀挀琀椀漀渀 伀渀䐀漀挀甀洀攀渀琀伀瀀攀渀䌀漀洀瀀氀攀琀攀 ⠀⤀ 笀ഀഀ
   Application.Run ("LIBRARY - Document level functions");਍   䄀瀀瀀氀椀挀愀琀椀漀渀⸀刀甀渀 ⠀∀䰀䤀䈀刀䄀刀夀 ⴀ 刀攀最椀猀琀爀礀 昀甀渀挀琀椀漀渀猀∀⤀㬀ഀഀ
   var root = ActiveDocument.DocumentElement;਍   挀漀渀瘀攀爀琀匀瀀愀挀攀猀䤀渀琀漀一䈀匀倀 ⠀⤀㬀  ⼀⼀ 䌀漀渀瘀攀爀琀 愀氀氀 洀甀氀琀椀 猀瀀愀挀攀猀 椀渀琀漀 ☀渀戀猀瀀㬀ഀഀ
}਍伀渀䐀漀挀甀洀攀渀琀伀瀀攀渀䌀漀洀瀀氀攀琀攀 ⠀⤀㬀ഀഀ
]]>਍㰀⼀䴀䄀䌀刀伀㸀ഀഀ
਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀伀渀开䈀攀昀漀爀攀开䐀漀挀甀洀攀渀琀开倀爀攀瘀椀攀眀∀ 栀椀搀攀㴀∀琀爀甀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
਍⼀⼀ ⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀ഀഀ
// DESCRIPTION਍⼀⼀ 吀栀攀 伀渀开䈀攀昀漀爀攀开䐀漀挀甀洀攀渀琀开倀爀攀瘀椀攀眀 攀瘀攀渀琀 椀猀 琀爀椀最最攀爀攀搀 眀栀攀渀 琀栀攀ഀഀ
// user has selected Page Preview view or Preview in Browser.਍⼀⼀ 吀栀椀猀 瀀愀爀琀椀挀甀氀愀爀 攀瘀攀渀琀 椀猀 搀攀猀椀最渀攀搀 琀漀 瀀攀爀昀漀爀洀 琀栀攀 攀砀攀挀甀琀攀 ഀഀ
// the following functions:਍⼀⼀   ⴀ 䔀氀椀洀椀渀愀琀攀 愀渀礀 椀渀琀攀爀渀愀氀 搀攀挀氀愀爀愀琀椀漀渀猀⸀ഀഀ
//   - Verify that a preview stylesheet has been selected.਍⼀⼀   ⴀ 吀爀愀渀猀昀漀爀洀 琀栀攀 搀漀挀甀洀攀渀琀 愀挀挀漀爀搀椀渀最 琀漀 琀栀攀 猀琀礀氀攀猀栀攀攀琀⸀ഀഀ
//   - Save the resulting document.਍⼀⼀   ⴀ 倀漀椀渀琀 琀栀攀 瀀爀攀瘀椀攀眀 唀刀䰀 琀漀 琀栀攀 爀攀猀甀氀琀椀渀最 搀漀挀甀洀攀渀琀⸀ഀഀ
//਍⼀⼀ 一伀吀䔀匀ഀഀ
// HISTORY਍⼀⼀ ㄀⸀ ㈀　　㈀　㤀㈀㐀  䌀伀刀䔀䰀㨀 䐀愀瘀椀搀 一最漀ഀഀ
// ************************************************************਍ഀഀ
function setAreaVPath (objXML) {਍   瘀愀爀 渀漀搀䰀椀猀琀㬀ഀഀ
   var strAttrValue;਍   瘀愀爀 搀漀挀㴀䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀㬀ഀഀ
   var areaPath=GetAttrValue(doc,"interwoven","areaVPath");਍   椀昀⠀ 愀爀攀愀倀愀琀栀 㴀㴀 渀甀氀氀 ⤀ 爀攀琀甀爀渀 ⴀ㄀㬀ഀഀ
   nodList = objXML.getElementsByTagName ("EPSPrint");਍   瘀愀爀 椀 㴀 　㬀ഀഀ
   while (i < nodList.length) {਍      猀琀爀䄀琀琀爀嘀愀氀甀攀 㴀 渀漀搀䰀椀猀琀⸀椀琀攀洀⠀椀⤀⸀最攀琀䄀琀琀爀椀戀甀琀攀⠀∀䘀椀氀攀一愀洀攀∀⤀㬀ഀഀ
      strAttrValue = strAttrValue.replace (/iwts:/, areaPath);਍⼀⼀      猀琀爀䄀琀琀爀嘀愀氀甀攀 㴀 ∀栀琀琀瀀㨀⼀⼀㄀㘀㠀⸀㄀㄀㘀⸀㌀㄀⸀㈀㄀㜀⼀椀眀ⴀ洀漀甀渀琀∀ ⬀ 猀琀爀䄀琀琀爀嘀愀氀甀攀㬀ഀഀ
      nodList.item(i).setAttribute ("FileName", strAttrValue);਍      椀 㴀 椀 ⬀ ㄀㬀ഀഀ
   }਍紀ഀഀ
਍昀甀渀挀琀椀漀渀 䤀猀䌀刀䤀琀攀洀 ⠀⤀ 笀ഀഀ
   var nodList;਍   瘀愀爀 猀琀爀䄀琀琀爀嘀愀氀甀攀㬀ഀഀ
   var doc=Application.ActiveDocument;਍   渀漀搀䰀椀猀琀 㴀 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀最攀琀䔀氀攀洀攀渀琀猀䈀礀吀愀最一愀洀攀 ⠀∀䤀琀攀洀∀⤀㬀ഀഀ
   var i = 0;਍   眀栀椀氀攀 ⠀椀 㰀 渀漀搀䰀椀猀琀⸀氀攀渀最琀栀⤀ 笀ഀഀ
      strAttrValue = nodList.item(i).getAttribute("ItemType");਍      椀昀 ⠀猀琀爀䄀琀琀爀嘀愀氀甀攀 㴀㴀 ∀䌀刀∀⤀ 笀ഀഀ
         return true;਍      紀ഀഀ
      i++;਍   紀ഀഀ
   return false;਍紀ഀഀ
਍昀甀渀挀琀椀漀渀 伀渀䈀攀昀漀爀攀䐀漀挀甀洀攀渀琀倀爀攀瘀椀攀眀堀䴀䰀 ⠀椀渀琀刀攀琀甀爀渀嘀愀氀甀攀Ⰰ 匀栀漀眀䄀渀猀眀攀爀䌀栀漀椀挀攀⤀ 笀ഀഀ
   var objFSO;਍   瘀愀爀 漀戀樀堀䴀䰀㬀ഀഀ
   var objXSL;਍   瘀愀爀 漀戀樀匀琀爀攀愀洀㬀ഀഀ
   var strText;਍   瘀愀爀 猀琀爀䈀爀漀眀猀攀爀倀爀攀瘀椀攀眀䘀椀氀攀㬀ഀഀ
   var strXSLFilename;਍   瘀愀爀 猀琀爀伀甀琀瀀甀琀㬀ഀഀ
   var strPreviewFilename;਍   瘀愀爀 猀琀爀䘀甀渀挀琀椀漀渀一愀洀攀㬀ഀഀ
   var strFOPFilename;਍   瘀愀爀 倀䐀䘀吀椀洀攀伀甀琀 㴀 ㌀　　㬀   ⼀⼀ 㔀 䴀椀渀甀琀攀猀 戀礀 搀攀昀愀甀氀琀⸀ഀഀ
਍   猀琀爀䘀甀渀挀琀椀漀渀一愀洀攀 㴀 ∀嬀䴀䄀䌀刀伀崀 伀渀开䈀攀昀漀爀攀开䐀漀挀甀洀攀渀琀开倀爀攀瘀椀攀眀∀㬀ഀഀ
 ਍   琀爀礀 笀ഀഀ
      var objFSO = new ActiveXObject( "Scripting.FileSystemObject" );਍ऀഀഀ
      // Eliminate internal declarations.਍      猀琀爀䈀爀漀眀猀攀爀倀爀攀瘀椀攀眀䘀椀氀攀 㴀 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀䌀爀攀愀琀攀倀爀攀瘀椀攀眀䘀椀氀攀⠀⤀㬀ഀഀ
      // Verify that a preview stylesheet has been selected.਍      猀琀爀堀匀䰀䘀椀氀攀渀愀洀攀 㴀 䄀瀀瀀氀椀挀愀琀椀漀渀⸀倀愀琀栀 ⬀ ∀尀尀䐀椀猀瀀氀愀礀尀尀刀㈀开䘀氀愀猀栀开唀䤀⸀砀猀氀∀ഀഀ
      if (! objFSO.FileExists( strXSLFilename ) ) {਍         椀昀 ⠀ 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀䌀甀猀琀漀洀䐀漀挀甀洀攀渀琀倀爀漀瀀攀爀琀椀攀猀⸀椀琀攀洀⠀∀䐀漀挀甀洀攀渀琀⸀一漀匀琀礀氀攀猀栀攀攀琀匀攀氀攀挀琀攀搀∀⤀ 㴀㴀 一漀琀栀椀渀最 ⤀ 笀ഀഀ
            Application.Alert ("Error in (" + strFunctionName + "), No Style Sheet avaiable: " + strXSLFilename);਍         紀ഀഀ
         objFSO = null;਍         爀攀琀甀爀渀㬀ऀഀഀ
      }	਍   紀 挀愀琀挀栀 ⠀攀⤀ 笀ഀഀ
      objFSO = null;਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀 ⠀∀䔀爀爀漀爀 椀渀 ⠀∀ ⬀ 猀琀爀䘀甀渀挀琀椀漀渀一愀洀攀 ⬀ ∀⤀Ⰰ 唀渀愀戀氀攀 琀漀 最攀琀 堀匀䰀 昀椀氀攀 昀漀爀 瀀爀攀瘀椀攀眀 㨀 ∀ ⬀ 猀琀爀堀匀䰀䘀椀氀攀渀愀洀攀⤀㬀ഀഀ
      return;਍   紀ഀഀ
   // Get AreaVPath from document.਍   瘀愀爀 搀漀挀㴀䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀㬀ഀഀ
   var areaPath = null;਍   愀爀攀愀倀愀琀栀㴀䜀攀琀䄀琀琀爀嘀愀氀甀攀⠀搀漀挀Ⰰ∀椀渀琀攀爀眀漀瘀攀渀∀Ⰰ∀愀爀攀愀嘀倀愀琀栀∀⤀㬀ഀഀ
   if (areaPath == null) {਍      愀爀攀愀倀愀琀栀 㴀  最攀琀䄀爀攀愀嘀倀愀琀栀䘀爀漀洀䰀愀甀渀挀栀倀愀搀⠀⤀㬀 ⼀⼀ 䜀攀琀 嘀倀愀琀栀 昀爀漀洀 氀漀挀愀氀 昀椀氀攀猀礀猀琀攀洀⸀ഀഀ
   }਍   椀昀⠀ 愀爀攀愀倀愀琀栀 㴀㴀 渀甀氀氀 ⤀ 笀ഀഀ
      Application.Alert ("Error in (" + strFunctionName + "), Unable to get VPath for images.");਍      䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀䌀甀猀琀漀洀䐀漀挀甀洀攀渀琀倀爀漀瀀攀爀琀椀攀猀⸀愀搀搀⠀∀匀眀椀琀挀栀嘀椀攀眀∀Ⰰ 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀倀爀攀瘀椀漀甀猀嘀椀攀眀吀礀瀀攀⤀㬀ഀഀ
      return -1;਍   紀ഀഀ
   // Removes \t \r \n in the preview file, Xmetal puts these in.਍   猀琀爀䘀伀倀䘀椀氀攀渀愀洀攀 㴀  䄀瀀瀀氀椀挀愀琀椀漀渀⸀倀愀琀栀 ⬀ ∀尀尀匀愀洀瀀氀攀猀尀尀䌀吀䈀尀尀刀㈀开䘀氀愀猀栀开唀䤀⸀昀漀∀ഀഀ
   try {਍      漀戀樀匀琀爀攀愀洀 㴀 漀戀樀䘀匀伀⸀伀瀀攀渀吀攀砀琀䘀椀氀攀 ⠀猀琀爀䈀爀漀眀猀攀爀倀爀攀瘀椀攀眀䘀椀氀攀Ⰰ ㄀Ⰰ 昀愀氀猀攀⤀㬀ഀഀ
      strText = objStream.ReadAll();਍      猀琀爀吀攀砀琀 㴀 猀琀爀吀攀砀琀⸀爀攀瀀氀愀挀攀 ⠀⼀椀眀琀猀㨀⼀最Ⰰ 愀爀攀愀倀愀琀栀⤀㬀ഀഀ
      strText = strText.replace (/\t|\r|\n/g, "");਍      猀琀爀吀攀砀琀 㴀 猀琀爀吀攀砀琀⸀爀攀瀀氀愀挀攀 ⠀⼀尀☀渀戀猀瀀㬀⼀最Ⰰ ∀ ∀⤀㬀ഀഀ
      objStream.Close();਍      ⼀⼀漀戀樀䘀匀伀⸀䐀攀氀攀琀攀䘀椀氀攀 ⠀ 猀琀爀䈀爀漀眀猀攀爀倀爀攀瘀椀攀眀䘀椀氀攀 ⤀㬀ഀഀ
਍      椀昀 ⠀ 漀戀樀䘀匀伀⸀䘀椀氀攀䔀砀椀猀琀猀⠀ 猀琀爀䘀伀倀䘀椀氀攀渀愀洀攀 ⤀⤀ 笀ഀഀ
         objFSO.DeleteFile ( strFOPFilename, true );਍      紀ऀഀഀ
      objStream = objFSO.CreateTextFile( strFOPFilename, true, false );਍      漀戀樀匀琀爀攀愀洀⸀圀爀椀琀攀⠀猀琀爀吀攀砀琀⤀㬀ഀഀ
      objStream.Close ();਍   紀 挀愀琀挀栀 ⠀攀⤀ 笀ഀഀ
      Application.Alert ("Error in (" + strFunctionName + "), Unable to cleanup file for PDF Preview.");਍      漀戀樀匀琀爀攀愀洀 㴀 渀甀氀氀㬀ഀഀ
      return;਍   紀ഀഀ
਍   琀爀礀 笀ഀഀ
      // Load the XML document into MSXML਍      漀戀樀堀䴀䰀 㴀 渀攀眀 䄀挀琀椀瘀攀堀伀戀樀攀挀琀⠀ ∀䴀猀砀洀氀㈀⸀䐀伀䴀䐀漀挀甀洀攀渀琀⸀㌀⸀　∀ ⤀㬀ഀഀ
   } catch (e) {਍      ⼀⼀漀戀樀堀䴀䰀 㴀 渀甀氀氀㬀ഀഀ
      Application.Alert("Error in (" + strFunctionName + "), Please verify that you have the Microsoft MSXML Parser Installed");਍      爀攀琀甀爀渀㬀ഀഀ
   }਍   漀戀樀堀䴀䰀⸀愀猀礀渀挀 㴀 昀愀氀猀攀㬀ഀഀ
   objXML.validateOnParse = false;਍   ⼀⼀漀戀樀堀䴀䰀⸀氀漀愀搀⠀䄀瀀瀀氀椀挀愀琀椀漀渀⸀倀愀琀栀吀漀唀爀氀⠀猀琀爀䈀爀漀眀猀攀爀倀爀攀瘀椀攀眀䘀椀氀攀⤀⤀㬀ഀഀ
   objXML.load(Application.PathToUrl(strBrowserPreviewFile));਍   椀昀 ⠀ 漀戀樀堀䴀䰀⸀瀀愀爀猀攀䔀爀爀漀爀⸀攀爀爀漀爀䌀漀搀攀 ℀㴀 　 ⤀ 笀ഀഀ
      Application.Alert ("Error in (" + strFunctionName + "), This XML document could not be loaded into MSXML due to the following parse error: "  + objXML.parseError.errorCode + objXML.parseError.description + "Browser Preview cancelled", "On Browser Preview" );਍      椀昀 ⠀ 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀倀爀攀瘀椀漀甀猀嘀椀攀眀吀礀瀀攀 㴀 猀焀嘀椀攀眀䈀爀漀眀猀攀 ⤀ 笀ഀഀ
         ActiveDocument.ViewType = sqViewTagsOn;਍      紀 攀氀猀攀 笀ഀഀ
         ActiveDocument.ViewType = ActiveDocument.PreviousViewType;਍      紀ऀഀഀ
      objXML = null;਍      漀戀樀䘀匀伀 㴀 渀甀氀氀㬀ഀഀ
      return;਍   紀ऀഀഀ
   // set areaVPath for images਍   猀攀琀䄀爀攀愀嘀倀愀琀栀 ⠀漀戀樀堀䴀䰀⤀㬀ഀഀ
   // Create FO File.਍   琀爀礀 笀ഀഀ
      // Load the XSL stylesheet਍      漀戀樀堀匀䰀 㴀 渀攀眀 䄀挀琀椀瘀攀堀伀戀樀攀挀琀⠀ ∀䴀猀砀洀氀㈀⸀䐀伀䴀䐀漀挀甀洀攀渀琀⸀㌀⸀　∀ ⤀㬀ഀഀ
      objXSL.async = false;਍      漀戀樀堀匀䰀⸀瘀愀氀椀搀愀琀攀伀渀倀愀爀猀攀 㴀 昀愀氀猀攀㬀ഀഀ
   } catch (e) {਍      漀戀樀堀䴀䰀 㴀 渀甀氀氀㬀ഀഀ
      Application.Alert( "Error in (" + strFunctionName + "), Please verify that you have the Microsoft MSXML Parser Installed");਍      爀攀琀甀爀渀㬀ഀഀ
   }਍   漀戀樀堀匀䰀⸀氀漀愀搀⠀ 䄀瀀瀀氀椀挀愀琀椀漀渀⸀倀愀琀栀吀漀唀刀䰀⠀ 猀琀爀堀匀䰀䘀椀氀攀渀愀洀攀 ⤀ ⤀㬀ഀഀ
   if ( objXSL.parseError.errorCode != 0 ) {਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀 ⠀∀䔀爀爀漀爀 椀渀 ⠀∀ ⬀ 猀琀爀䘀甀渀挀琀椀漀渀一愀洀攀 ⬀ ∀⤀Ⰰ 堀匀䰀 猀琀礀氀攀猀栀攀攀琀 瀀愀爀猀攀 攀爀爀漀爀㨀 ∀ ⬀ 漀戀樀堀匀䰀⸀瀀愀爀猀攀䔀爀爀漀爀⸀爀攀愀猀漀渀Ⰰ 琀爀甀攀 ⤀㬀ഀഀ
      ActiveDocument.ViewType = ActiveDocument.PreviousViewType;਍      漀戀樀堀䴀䰀 㴀 渀甀氀氀㬀ഀഀ
      objFSO = null;਍      爀攀琀甀爀渀㬀ഀഀ
   }਍ഀഀ
   // Transform node਍   猀琀爀伀甀琀瀀甀琀 㴀 漀戀樀堀䴀䰀⸀琀爀愀渀猀昀漀爀洀一漀搀攀⠀ 漀戀樀堀匀䰀 ⤀ഀഀ
   if ( objXML.parseError.errorCode != 0 ) {਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀 ⠀∀䔀爀爀漀爀 椀渀 ⠀∀ ⬀ 猀琀爀䘀甀渀挀琀椀漀渀一愀洀攀 ⬀ ∀⤀Ⰰ 堀匀䰀 琀爀愀渀猀昀漀爀洀愀琀椀漀渀 攀爀爀漀爀㨀 ∀ ⬀ 漀戀樀堀䴀䰀⸀瀀愀爀猀攀䔀爀爀漀爀⸀爀攀愀猀漀渀Ⰰ 琀爀甀攀 ⤀㬀ഀഀ
      ActiveDocument.ViewType = ActiveDocument.PreviousViewType;਍      漀戀樀䘀匀伀⸀䐀攀氀攀琀攀䘀椀氀攀 ⠀ 䄀瀀瀀氀椀挀愀琀椀漀渀⸀唀刀䰀琀漀倀愀琀栀 ⠀ 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀䈀爀漀眀猀攀爀唀刀䰀 ⤀ ⤀㬀ഀഀ
      objXML = null;਍      漀戀樀䘀匀伀 㴀 渀甀氀氀㬀ഀഀ
      return;	਍   紀ऀഀഀ
਍   ⼀⼀ 匀愀瘀攀 琀栀攀 爀攀猀甀氀琀椀渀最 搀漀挀甀洀攀渀琀⸀ഀഀ
   strPreviewFilename =  Application.Path + "\\Samples\\CTB\\R2_Flash_UI.htm"਍   ⼀⼀猀琀爀䘀伀倀唀刀䰀 㴀 ∀栀琀琀瀀㨀⼀⼀㄀㤀㠀⸀㐀㔀⸀㄀㜀⸀㈀㌀㨀㠀　　　⼀昀漀瀀⼀昀漀瀀∀㬀ഀഀ
   if (Application.CustomProperties.item("FOPURL") != null) {਍      猀琀爀䘀伀倀唀刀䰀 㴀 䄀瀀瀀氀椀挀愀琀椀漀渀⸀䌀甀猀琀漀洀倀爀漀瀀攀爀琀椀攀猀⸀椀琀攀洀⠀∀䘀伀倀唀刀䰀∀⤀⸀嘀愀氀甀攀㬀ഀഀ
   } else {਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀 ⠀∀䘀伀倀唀刀䰀 椀渀 琀栀攀 砀洀挀甀猀⸀椀渀椀 昀椀氀攀 椀猀 渀漀琀 愀瘀愀椀愀戀氀攀⸀∀⤀㬀ഀഀ
   }਍   椀昀 ⠀䄀瀀瀀氀椀挀愀琀椀漀渀⸀䌀甀猀琀漀洀倀爀漀瀀攀爀琀椀攀猀⸀椀琀攀洀⠀∀䘀伀倀唀刀䰀圀椀琀栀䄀渀猀眀攀爀䬀攀礀∀⤀ ℀㴀 渀甀氀氀⤀ 笀ഀഀ
      strFOPURLWithAnswerKey = Application.CustomProperties.item("FOPURLWithAnswerKey").Value;਍   紀 攀氀猀攀 笀ഀഀ
      Application.Alert ("FOPURLWithAnswerKey in the xmcus.ini file is not avaiable.");਍   紀ഀഀ
   if (Application.CustomProperties.item("PDFTimeOut") != null) {਍      倀䐀䘀吀椀洀攀伀甀琀 㴀 䄀瀀瀀氀椀挀愀琀椀漀渀⸀䌀甀猀琀漀洀倀爀漀瀀攀爀琀椀攀猀⸀椀琀攀洀⠀∀倀䐀䘀吀椀洀攀伀甀琀∀⤀⸀嘀愀氀甀攀㬀ഀഀ
   } else {਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀 ⠀∀倀䐀䘀吀椀洀攀伀甀琀 椀渀 琀栀攀 砀洀挀甀猀⸀椀渀椀 昀椀氀攀 椀猀 渀漀琀 愀瘀愀椀愀戀氀攀⸀∀⤀㬀ഀഀ
   }਍ഀഀ
   if ( objFSO.FileExists( strPreviewFilename )) {਍      漀戀樀䘀匀伀⸀䐀攀氀攀琀攀䘀椀氀攀 ⠀ 猀琀爀倀爀攀瘀椀攀眀䘀椀氀攀渀愀洀攀Ⰰ 琀爀甀攀 ⤀㬀ഀഀ
   }	਍   ⼀⼀ ㈀　　㌀　㌀　㔀 䐀一 ⴀ 䐀攀氀攀琀椀渀最 琀栀攀 䈀爀漀眀猀攀爀倀爀攀瘀椀攀眀䘀椀氀攀 挀爀攀愀琀攀搀 愀 瀀爀漀戀氀攀洀 眀栀攀渀ഀഀ
   // closing the document from the browser windows for Interwoven Component਍   ⼀⼀ 昀椀氀攀 ⠀昀椀氀攀猀 椀渀 琀栀攀 䌀漀洀瀀漀渀攀渀琀 搀椀爀攀挀琀漀爀礀⤀ Ⰰ 椀琀 眀漀爀欀猀 昀椀渀攀 昀漀爀 甀猀攀爀 挀爀攀愀琀攀搀ഀഀ
   // Component file. For whatever reason, XMetal does not create a new਍   ⼀⼀ 倀爀攀瘀椀攀眀䘀椀氀攀 昀漀爀 䌀漀洀瀀漀渀攀渀琀 昀椀氀攀⸀ 吀栀攀爀攀昀漀爀攀Ⰰ 椀昀 搀攀氀攀琀攀 琀栀攀 瀀爀攀瘀椀攀眀 昀椀氀攀ഀഀ
਍   ⼀⼀ 眀漀甀氀搀 愀氀猀漀 搀攀氀攀琀攀 琀栀攀 攀搀椀琀椀渀最 昀椀氀攀⸀ഀഀ
਍   ⼀⼀ 漀戀樀䘀匀伀⸀䐀攀氀攀琀攀䘀椀氀攀 ⠀ 猀琀爀䈀爀漀眀猀攀爀倀爀攀瘀椀攀眀䘀椀氀攀 ⤀㬀ഀഀ
਍   ⼀⼀ 䌀栀攀挀欀 猀攀攀 椀昀 䄀爀挀漀戀愀琀 刀攀愀搀攀爀 椀猀 椀渀猀琀愀氀氀攀搀⸀ഀഀ
   if (! KeyExist ("HKCU\\Software\\Adobe\\Acrobat Reader\\")) {਍      猀琀爀倀爀攀瘀椀攀眀䘀椀氀攀渀愀洀攀 㴀  䄀瀀瀀氀椀挀愀琀椀漀渀⸀倀愀琀栀 ⬀ ∀尀尀挀漀渀昀椀最尀尀挀琀戀尀尀瀀爀攀瘀椀攀眀尀尀䄀挀爀漀戀愀琀开刀攀愀搀攀爀⸀栀琀洀∀ഀഀ
      ActiveDocument.BrowserURL = Application.PathToURL( strPreviewFilename );਍      爀攀琀甀爀渀㬀ഀഀ
   }਍ഀഀ
   try {਍      椀昀 ⠀椀渀琀刀攀琀甀爀渀嘀愀氀甀攀 㴀㴀 ㄀⤀ 笀ഀഀ
         var BrView = new ActiveXObject ("FileUpload.UploadClass1");਍      紀 攀氀猀攀 椀昀 ⠀椀渀琀刀攀琀甀爀渀嘀愀氀甀攀 㴀㴀 㐀⤀ 笀ഀഀ
         var BrView = new ActiveXObject ("FileUploadBrowser.UploadClass1");਍      紀ഀഀ
      //BrView.UploadFileToFOP();਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀匀攀琀䌀甀爀猀漀爀⠀㈀⤀㬀ഀഀ
      Application.SetStatusText ("File being transformed for PDF.  This may take several seconds.  Please wait . . . . .");਍      椀昀 ⠀匀栀漀眀䄀渀猀眀攀爀䌀栀漀椀挀攀⤀ 笀ഀഀ
         // Render PDF With Answer Key by send to a different URL.਍         䈀爀嘀椀攀眀⸀唀瀀氀漀愀搀䘀椀氀攀吀漀䘀伀倀⠀猀琀爀䘀伀倀唀刀䰀圀椀琀栀䄀渀猀眀攀爀䬀攀礀Ⰰ 猀琀爀䘀伀倀䘀椀氀攀渀愀洀攀Ⰰ 猀琀爀倀爀攀瘀椀攀眀䘀椀氀攀渀愀洀攀Ⰰ 倀䐀䘀吀椀洀攀伀甀琀⤀㬀ഀഀ
      } else {਍         䈀爀嘀椀攀眀⸀唀瀀氀漀愀搀䘀椀氀攀吀漀䘀伀倀⠀猀琀爀䘀伀倀唀刀䰀Ⰰ 猀琀爀䘀伀倀䘀椀氀攀渀愀洀攀Ⰰ 猀琀爀倀爀攀瘀椀攀眀䘀椀氀攀渀愀洀攀Ⰰ 倀䐀䘀吀椀洀攀伀甀琀⤀㬀ഀഀ
      }਍      椀昀 ⠀椀渀琀刀攀琀甀爀渀嘀愀氀甀攀 㴀㴀 㐀⤀ 笀ഀഀ
         ActiveDocument.CustomDocumentProperties.add("SwitchView", sqViewBrowse);਍      紀ഀഀ
   } catch (e) {਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀 ⠀∀䔀爀爀漀爀 椀渀 ⠀∀ ⬀ 猀琀爀䘀甀渀挀琀椀漀渀一愀洀攀 ⬀ ∀⤀Ⰰ 唀渀愀戀氀攀 琀漀 氀漀愀搀 䘀椀氀攀唀瀀氀漀愀搀⸀搀氀氀㨀 ∀ ⬀ 攀⸀搀攀猀挀爀椀瀀琀椀漀渀⤀㬀ഀഀ
      return;਍   紀ഀഀ
   ਍   䄀瀀瀀氀椀挀愀琀椀漀渀⸀匀攀琀匀琀愀琀甀猀吀攀砀琀 ⠀∀∀⤀㬀ഀഀ
   Application.SetCursor(0);਍   琀爀礀 笀ഀഀ
      ActiveDocument.BrowserURL = Application.PathToURL( strPreviewFilename );਍   紀 挀愀琀挀栀 ⠀攀⤀ 笀紀ഀഀ
਍   ⼀⼀ 䜀愀爀戀愀最攀 挀漀氀氀攀挀琀椀漀渀⸀ഀഀ
਍   䈀爀嘀椀攀眀 㴀 渀甀氀氀㬀ഀഀ
   objFSO = null;਍   漀戀樀堀䴀䰀 㴀 渀甀氀氀㬀ഀഀ
   objXSL = null;਍   漀戀樀匀琀爀攀愀洀 㴀 渀甀氀氀㬀ഀഀ
}਍ഀഀ
function getPreviewType () {਍   瘀愀爀 猀琀爀刀渀最吀攀砀琀 㴀 ∀∀㬀ഀഀ
   var swfPreviewFilename;਍   瘀愀爀 猀琀爀匀圀䘀䘀椀氀攀渀愀洀攀ഀഀ
   var strTargetApp;਍   瘀愀爀 猀琀爀吀愀爀最攀琀䘀椀攀氀搀 㴀 ∀洀礀堀䴀䰀䐀愀琀愀∀㬀ഀഀ
   var strFunctionName = "getPreviewType";਍   瘀愀爀 猀琀爀匀圀䘀唀刀䰀 㴀 渀甀氀氀㬀ഀഀ
   var strPreviewFilename;਍   瘀愀爀 猀琀爀䐀漀挀琀礀瀀攀䐀吀䐀㬀ഀഀ
   var SWFTimeOut = 300;   // 5 Minutes by default.਍   椀昀 ⠀⠀匀攀氀攀挀琀椀漀渀⸀䤀猀倀愀爀攀渀琀䔀氀攀洀攀渀琀 ⠀∀䤀琀攀洀∀⤀⤀ 簀簀 ⠀匀攀氀攀挀琀椀漀渀⸀䤀猀倀愀爀攀渀琀䔀氀攀洀攀渀琀 ⠀∀䤀琀攀洀匀攀琀∀⤀⤀ 簀簀 ⠀匀攀氀攀挀琀椀漀渀⸀䤀猀倀愀爀攀渀琀䔀氀攀洀攀渀琀 ⠀∀匀甀戀吀攀猀琀∀⤀⤀ 簀簀 ⠀匀攀氀攀挀琀椀漀渀⸀䤀猀倀愀爀攀渀琀䔀氀攀洀攀渀琀 ⠀∀䄀猀猀攀猀猀洀攀渀琀∀⤀⤀⤀ 笀ഀഀ
   } else {਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀 ⠀∀䐀漀挀甀洀攀渀琀 洀甀猀琀 挀漀渀琀愀椀渀 愀琀氀攀愀猀琀 漀渀攀 䤀琀攀洀 戀攀昀漀爀攀 倀爀攀瘀椀攀眀椀渀最⸀∀⤀㬀ഀഀ
      ActiveDocument.CustomDocumentProperties.add("SwitchView", ActiveDocument.PreviousViewType);਍      爀攀琀甀爀渀㬀ഀഀ
   }਍   椀昀 ⠀䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀嘀椀攀眀吀礀瀀攀 㴀㴀 ㈀⤀ 笀ഀഀ
      Application.Alert ("Please switch over to Normal or Tags On view first before Previewing.");਍      䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀䌀甀猀琀漀洀䐀漀挀甀洀攀渀琀倀爀漀瀀攀爀琀椀攀猀⸀愀搀搀⠀∀匀眀椀琀挀栀嘀椀攀眀∀Ⰰ 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀倀爀攀瘀椀漀甀猀嘀椀攀眀吀礀瀀攀⤀㬀ഀഀ
      return;਍   紀ഀഀ
   var objFSO = new ActiveXObject( "Scripting.FileSystemObject" );਍   椀昀 ⠀䤀猀䌀刀䤀琀攀洀⠀⤀⤀ 笀ഀഀ
      if (ActiveDocument.ViewType == sqViewBrowse) {਍         瘀愀爀 猀琀爀倀愀琀栀 㴀 䄀瀀瀀氀椀挀愀琀椀漀渀⸀倀愀琀栀 ⬀ ∀尀尀挀漀渀昀椀最尀尀挀琀戀尀尀昀漀爀洀猀尀尀倀爀攀瘀椀攀眀伀瀀琀椀漀渀猀圀椀琀栀䄀渀猀眀攀爀匀攀瀀愀爀愀琀攀圀椀渀搀漀眀⸀砀昀琀∀㬀ഀഀ
      } else {਍         瘀愀爀 猀琀爀倀愀琀栀 㴀 䄀瀀瀀氀椀挀愀琀椀漀渀⸀倀愀琀栀 ⬀ ∀尀尀挀漀渀昀椀最尀尀挀琀戀尀尀昀漀爀洀猀尀尀倀爀攀瘀椀攀眀伀瀀琀椀漀渀猀圀椀琀栀䄀渀猀眀攀爀⸀砀昀琀∀㬀ഀഀ
      }਍   紀 攀氀猀攀 笀ഀഀ
      if (ActiveDocument.ViewType == sqViewBrowse) {਍         瘀愀爀 猀琀爀倀愀琀栀 㴀 䄀瀀瀀氀椀挀愀琀椀漀渀⸀倀愀琀栀 ⬀ ∀尀尀挀漀渀昀椀最尀尀挀琀戀尀尀昀漀爀洀猀尀尀倀爀攀瘀椀攀眀伀瀀琀椀漀渀猀匀攀瀀愀爀愀琀攀圀椀渀搀漀眀⸀砀昀琀∀㬀ഀഀ
      } else {਍         瘀愀爀 猀琀爀倀愀琀栀 㴀 䄀瀀瀀氀椀挀愀琀椀漀渀⸀倀愀琀栀 ⬀ ∀尀尀挀漀渀昀椀最尀尀挀琀戀尀尀昀漀爀洀猀尀尀倀爀攀瘀椀攀眀伀瀀琀椀漀渀猀⸀砀昀琀∀㬀ഀഀ
      }਍   紀ഀഀ
   var intReturnValue = true;਍   琀爀礀 笀ഀഀ
      var objdlg = Application.CreateFormDlg(strPath);਍   紀 挀愀琀挀栀 ⠀攀⤀ 笀ഀഀ
      Application.Alert ("The Preview form can not be located (" + strPath + ")");਍      䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀䌀甀猀琀漀洀䐀漀挀甀洀攀渀琀倀爀漀瀀攀爀琀椀攀猀⸀愀搀搀⠀∀匀眀椀琀挀栀嘀椀攀眀∀Ⰰ 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀倀爀攀瘀椀漀甀猀嘀椀攀眀吀礀瀀攀⤀㬀ഀഀ
      return;਍   紀ഀഀ
   objdlg.DoModal();਍   椀昀 ⠀⠀漀戀樀搀氀最⸀琀砀琀䈀漀砀⸀琀攀砀琀 㴀㴀 ∀伀䬀∀⤀ 簀簀 ⠀漀戀樀搀氀最⸀琀砀琀䈀漀砀⸀琀攀砀琀 㴀㴀 ∀伀䬀开圀椀琀栀开䄀渀猀眀攀爀䬀攀礀∀⤀⤀ 笀ഀഀ
      intReturnValue = objdlg.RBViewType.Value;਍      椀昀 ⠀⠀椀渀琀刀攀琀甀爀渀嘀愀氀甀攀 㴀㴀 ㄀⤀ 簀簀 ⠀椀渀琀刀攀琀甀爀渀嘀愀氀甀攀 㴀㴀 㐀⤀⤀ 笀  ⼀⼀ 倀䐀䘀 嘀椀攀眀⸀ഀഀ
         if (objdlg.txtBox.text == "OK_With_AnswerKey") {਍            伀渀䈀攀昀漀爀攀䐀漀挀甀洀攀渀琀倀爀攀瘀椀攀眀堀䴀䰀⠀椀渀琀刀攀琀甀爀渀嘀愀氀甀攀Ⰰ 琀爀甀攀⤀㬀ഀഀ
         } else {਍            伀渀䈀攀昀漀爀攀䐀漀挀甀洀攀渀琀倀爀攀瘀椀攀眀堀䴀䰀⠀椀渀琀刀攀琀甀爀渀嘀愀氀甀攀Ⰰ 昀愀氀猀攀⤀㬀ഀഀ
         }਍      紀 攀氀猀攀 笀        ⼀⼀ 匀圀䘀 倀爀攀瘀椀攀眀⸀ഀഀ
          // Save the resulting document.਍         猀琀爀倀爀攀瘀椀攀眀䘀椀氀攀渀愀洀攀 㴀  䄀瀀瀀氀椀挀愀琀椀漀渀⸀倀愀琀栀 ⬀ ∀尀尀匀愀洀瀀氀攀猀尀尀䌀吀䈀尀尀刀㈀开䘀氀愀猀栀开唀䤀⸀栀琀洀∀ഀഀ
         //strSWFURL = "http://198.45.17.23:8000/servlet/parserupload";਍         椀昀 ⠀䄀瀀瀀氀椀挀愀琀椀漀渀⸀䌀甀猀琀漀洀倀爀漀瀀攀爀琀椀攀猀⸀椀琀攀洀⠀∀匀圀䘀唀刀䰀∀⤀ ℀㴀 渀甀氀氀⤀ 笀ഀഀ
            strSWFURL = Application.CustomProperties.item("SWFURL").Value;਍         紀 攀氀猀攀 笀ഀഀ
            Application.Alert ("SWFURL in the xmcus.ini file is not avaiable.");਍            䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀䌀甀猀琀漀洀䐀漀挀甀洀攀渀琀倀爀漀瀀攀爀琀椀攀猀⸀愀搀搀⠀∀匀眀椀琀挀栀嘀椀攀眀∀Ⰰ 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀倀爀攀瘀椀漀甀猀嘀椀攀眀吀礀瀀攀⤀㬀ഀഀ
            return;਍         紀ഀഀ
         if (Application.CustomProperties.item("SWFTimeOut") != null) {਍            匀圀䘀吀椀洀攀伀甀琀 㴀 䄀瀀瀀氀椀挀愀琀椀漀渀⸀䌀甀猀琀漀洀倀爀漀瀀攀爀琀椀攀猀⸀椀琀攀洀⠀∀匀圀䘀吀椀洀攀伀甀琀∀⤀⸀嘀愀氀甀攀㬀ഀഀ
         } else {਍            䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀 ⠀∀匀圀䘀吀椀洀攀伀甀琀 椀渀 琀栀攀 砀洀挀甀猀⸀椀渀椀 昀椀氀攀 椀猀 渀漀琀 愀瘀愀椀愀戀氀攀⸀∀⤀㬀ഀഀ
            ActiveDocument.CustomDocumentProperties.add("SwitchView", ActiveDocument.PreviousViewType);਍            爀攀琀甀爀渀㬀ഀഀ
         }਍         ⼀⼀ 猀琀爀䐀漀挀琀礀瀀攀䐀吀䐀 㴀 ∀栀琀琀瀀㨀⼀⼀㄀㤀㠀⸀㐀㔀⸀㄀㜀⸀㈀㌀㨀㠀　　　⼀刀㈀䐀吀䐀⼀刀㈀开䘀氀愀猀栀开唀䤀⸀搀琀搀∀ഀഀ
         if (Application.CustomProperties.item("SWFDoctypeDTD") != null) {਍            猀琀爀䐀漀挀琀礀瀀攀䐀吀䐀 㴀 䄀瀀瀀氀椀挀愀琀椀漀渀⸀䌀甀猀琀漀洀倀爀漀瀀攀爀琀椀攀猀⸀椀琀攀洀⠀∀匀圀䘀䐀漀挀琀礀瀀攀䐀吀䐀∀⤀⸀嘀愀氀甀攀㬀ഀഀ
         } else {਍            䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀 ⠀∀匀圀䘀䐀漀挀琀礀瀀攀䐀吀䐀 椀渀 琀栀攀 砀洀挀甀猀⸀椀渀椀 昀椀氀攀 椀猀 渀漀琀 愀瘀愀椀愀戀氀攀⸀∀⤀㬀ഀഀ
            ActiveDocument.CustomDocumentProperties.add("SwitchView", ActiveDocument.PreviousViewType);਍            爀攀琀甀爀渀㬀ഀഀ
         }਍         椀昀 ⠀ 漀戀樀䘀匀伀⸀䘀椀氀攀䔀砀椀猀琀猀⠀ 猀琀爀倀爀攀瘀椀攀眀䘀椀氀攀渀愀洀攀 ⤀⤀ 笀ഀഀ
            objFSO.DeleteFile ( strPreviewFilename, true );਍         紀ऀഀഀ
         if ((intReturnValue == 2) || (intReturnValue == 5)) { // SWF single Item view.਍⼀⼀            猀琀爀吀愀爀最攀琀䄀瀀瀀 㴀 ∀栀琀琀瀀㨀⼀⼀㄀㤀㠀⸀㐀㔀⸀㄀㜀⸀㈀㌀㨀㠀　　　⼀猀栀漀挀欀眀愀瘀攀琀攀洀瀀氀愀琀攀猀⼀挀愀戀开愀欀开椀琀攀洀⸀猀眀琀∀㬀ഀഀ
            if (Application.CustomProperties.item("SWFOneItem") != null) {਍               猀琀爀吀愀爀最攀琀䄀瀀瀀 㴀 䄀瀀瀀氀椀挀愀琀椀漀渀⸀䌀甀猀琀漀洀倀爀漀瀀攀爀琀椀攀猀⸀椀琀攀洀⠀∀匀圀䘀伀渀攀䤀琀攀洀∀⤀⸀嘀愀氀甀攀㬀ഀഀ
            } else {਍               䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀 ⠀∀匀圀䘀伀渀攀䤀琀攀洀 椀渀 琀栀攀 砀洀挀甀猀⸀椀渀椀 昀椀氀攀 椀猀 渀漀琀 愀瘀愀椀愀戀氀攀⸀∀⤀㬀ഀഀ
               ActiveDocument.CustomDocumentProperties.add("SwitchView", ActiveDocument.PreviousViewType);਍               爀攀琀甀爀渀㬀ഀഀ
            }਍ഀഀ
            // Get areaVPath for images਍            瘀愀爀 搀漀挀㴀䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀㬀ഀഀ
            var areaPath = null;਍            愀爀攀愀倀愀琀栀㴀䜀攀琀䄀琀琀爀嘀愀氀甀攀⠀搀漀挀Ⰰ∀椀渀琀攀爀眀漀瘀攀渀∀Ⰰ∀愀爀攀愀嘀倀愀琀栀∀⤀㬀ഀഀ
            if (areaPath == null) {਍               愀爀攀愀倀愀琀栀 㴀  最攀琀䄀爀攀愀嘀倀愀琀栀䘀爀漀洀䰀愀甀渀挀栀倀愀搀⠀⤀㬀 ⼀⼀ 䜀攀琀 嘀倀愀琀栀 昀爀漀洀 氀漀挀愀氀 昀椀氀攀猀礀猀琀攀洀⸀ഀഀ
            }਍            椀昀⠀ 愀爀攀愀倀愀琀栀 㴀㴀 渀甀氀氀 ⤀ 笀ഀഀ
               Application.Alert ("Error in (" + strFunctionName + "), Unable to get VPath for images.");਍               䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀䌀甀猀琀漀洀䐀漀挀甀洀攀渀琀倀爀漀瀀攀爀琀椀攀猀⸀愀搀搀⠀∀匀眀椀琀挀栀嘀椀攀眀∀Ⰰ 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀倀爀攀瘀椀漀甀猀嘀椀攀眀吀礀瀀攀⤀㬀ഀഀ
               return -1;਍            紀ഀഀ
    ਍            ⼀⼀ 䌀爀攀愀琀椀渀最 匀圀䘀 昀椀氀攀 昀漀爀 猀甀戀洀椀琀琀椀渀最 琀漀 䨀䜀攀渀 昀漀爀 䘀氀愀猀栀 倀爀攀瘀椀攀眀⸀ഀഀ
            strSWFFilename =  Application.Path + "\\Samples\\CTB\\R2_oneitem.xml"਍            瘀愀爀 爀渀最 㴀 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀刀愀渀最攀㬀ഀഀ
            if (rng.IsParentElement("Item")) {਍               眀栀椀氀攀 ⠀爀渀最⸀䌀漀渀琀愀椀渀攀爀一漀搀攀⸀渀漀搀攀一愀洀攀 ℀㴀 ∀䤀琀攀洀∀⤀ 笀ഀഀ
                  rng.SelectElement();਍               紀ഀഀ
               rng.SelectElement();਍               琀爀礀 笀ഀഀ
                  objStream = objFSO.CreateTextFile( strSWFFilename, true, false );਍⼀⼀                  漀戀樀匀琀爀攀愀洀⸀圀爀椀琀攀⠀∀㰀㼀砀洀氀 瘀攀爀猀椀漀渀㴀尀∀㄀⸀　尀∀ 攀渀挀漀搀椀渀最㴀尀∀唀吀䘀ⴀ㠀尀∀ 猀琀愀渀搀愀氀漀渀攀㴀尀∀渀漀尀∀㼀㸀㰀℀䐀伀䌀吀夀倀䔀 䤀琀攀洀 匀夀匀吀䔀䴀 尀∀栀琀琀瀀㨀⼀⼀㄀㤀㠀⸀㐀㔀⸀㄀㜀⸀㈀㌀㨀㠀　　　⼀刀㈀䐀吀䐀⼀刀㈀开䘀氀愀猀栀开唀䤀⸀搀琀搀尀∀㸀∀⤀㬀ഀഀ
                  objStream.Write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE Item SYSTEM ");਍                  漀戀樀匀琀爀攀愀洀⸀圀爀椀琀攀 ⠀∀尀∀∀ ⬀ 猀琀爀䐀漀挀琀礀瀀攀䐀吀䐀 ⬀ ∀尀∀∀ ⬀ ∀㸀∀⤀㬀ഀഀ
                  strRngText = rng.text;਍                  猀琀爀刀渀最吀攀砀琀 㴀 猀琀爀刀渀最吀攀砀琀⸀爀攀瀀氀愀挀攀 ⠀⼀椀眀琀猀㨀⼀最Ⰰ 愀爀攀愀倀愀琀栀⤀㬀ഀഀ
                  strRngText = strRngText.replace (/\t|\r|\n/g, "");਍                  猀琀爀刀渀最吀攀砀琀 㴀 猀琀爀刀渀最吀攀砀琀⸀爀攀瀀氀愀挀攀 ⠀⼀尀☀渀戀猀瀀㬀⼀最Ⰰ ∀ ∀⤀㬀ഀഀ
                  strRngText = strRngText.replace (/\&USpace;/g, " ");਍                  漀戀樀匀琀爀攀愀洀⸀圀爀椀琀攀⠀猀琀爀刀渀最吀攀砀琀⤀㬀ഀഀ
                  objStream.Close ();਍                  漀戀樀匀琀爀攀愀洀 㴀 渀甀氀氀㬀ഀഀ
               } catch (e) {਍                  䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀 ⠀∀䔀爀爀漀爀 椀渀 ⠀∀ ⬀ 猀琀爀䘀甀渀挀琀椀漀渀一愀洀攀 ⬀ ∀⤀Ⰰ 唀渀愀戀氀攀 琀漀 挀爀攀愀琀攀 昀椀氀攀 昀漀爀 䘀氀愀猀栀 倀爀攀瘀椀攀眀⸀尀渀尀渀䘀椀氀攀 洀愀礀 挀漀渀琀愀椀渀猀 唀渀椀挀漀搀攀 䌀栀愀爀愀挀琀攀爀猀 琀栀愀琀 椀猀 渀漀琀 猀甀瀀瀀漀爀琀攀搀 椀渀 䘀氀愀猀栀⸀∀⤀㬀ഀഀ
                  ActiveDocument.CustomDocumentProperties.add("SwitchView", ActiveDocument.PreviousViewType);਍                  漀戀樀匀琀爀攀愀洀 㴀 渀甀氀氀㬀ഀഀ
                  return;਍               紀ഀഀ
            ਍               ⼀⼀ 唀瀀氀漀愀搀椀渀最 匀圀䘀 昀椀氀攀 琀漀 䨀䜀攀渀 昀漀爀 䘀氀愀猀栀 倀爀攀瘀椀攀眀⸀ഀഀ
               try {਍                  椀昀 ⠀椀渀琀刀攀琀甀爀渀嘀愀氀甀攀 㴀㴀 ㈀⤀ 笀ഀഀ
                     var BrView = new ActiveXObject ("swfupload.UploadClass1");਍                  紀 攀氀猀攀 椀昀 ⠀椀渀琀刀攀琀甀爀渀嘀愀氀甀攀 㴀㴀 㔀⤀ 笀ഀഀ
                     var BrView = new ActiveXObject ("swfuploadBrowser.UploadClass1");਍                  紀ഀഀ
                  //BrView.UploadFileToFOP();਍                  䄀瀀瀀氀椀挀愀琀椀漀渀⸀匀攀琀䌀甀爀猀漀爀⠀㈀⤀㬀ഀഀ
                  Application.SetStatusText ("File being transformed for Flash.  This may take several seconds.  Please wait . . . . .");਍                  䈀爀嘀椀攀眀⸀唀瀀氀漀愀搀䘀椀氀攀吀漀䘀伀倀⠀猀琀爀匀圀䘀唀刀䰀Ⰰ 猀琀爀匀圀䘀䘀椀氀攀渀愀洀攀Ⰰ 猀琀爀倀爀攀瘀椀攀眀䘀椀氀攀渀愀洀攀Ⰰ 猀琀爀吀愀爀最攀琀䄀瀀瀀Ⰰ 猀琀爀吀愀爀最攀琀䘀椀攀氀搀Ⰰ 匀圀䘀吀椀洀攀伀甀琀⤀㬀ഀഀ
                  if (intReturnValue == 5) {    // Preview in Separate Windows.਍                     䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀䌀甀猀琀漀洀䐀漀挀甀洀攀渀琀倀爀漀瀀攀爀琀椀攀猀⸀愀搀搀⠀∀匀眀椀琀挀栀嘀椀攀眀∀Ⰰ 猀焀嘀椀攀眀䈀爀漀眀猀攀⤀㬀ഀഀ
                  }਍               紀 挀愀琀挀栀 ⠀攀⤀ 笀ഀഀ
                  Application.Alert ("Error in (" + strFunctionName + "), Unable to load SWFUpload.dll: " + e.description);਍                  䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀䌀甀猀琀漀洀䐀漀挀甀洀攀渀琀倀爀漀瀀攀爀琀椀攀猀⸀愀搀搀⠀∀匀眀椀琀挀栀嘀椀攀眀∀Ⰰ 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀倀爀攀瘀椀漀甀猀嘀椀攀眀吀礀瀀攀⤀㬀ഀഀ
                  return;਍               紀ഀഀ
               Application.SetStatusText ("");਍               䄀瀀瀀氀椀挀愀琀椀漀渀⸀匀攀琀䌀甀爀猀漀爀⠀　⤀㬀ഀഀ
               try {਍                  䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀䈀爀漀眀猀攀爀唀刀䰀 㴀 䄀瀀瀀氀椀挀愀琀椀漀渀⸀倀愀琀栀吀漀唀刀䰀⠀ 猀琀爀倀爀攀瘀椀攀眀䘀椀氀攀渀愀洀攀 ⤀㬀ഀഀ
               } catch (e) {}਍            紀 攀氀猀攀 笀ഀഀ
               Application.Alert ("Please position your cursor inside of an Item for Single Item Preview of SWF.");਍               䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀䌀甀猀琀漀洀䐀漀挀甀洀攀渀琀倀爀漀瀀攀爀琀椀攀猀⸀愀搀搀⠀∀匀眀椀琀挀栀嘀椀攀眀∀Ⰰ 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀倀爀攀瘀椀漀甀猀嘀椀攀眀吀礀瀀攀⤀㬀ഀഀ
               return;਍            紀ഀഀ
         } else if ((intReturnValue == 3) || (intReturnValue == 6)) { // SWF single Item view.਍            ⼀⼀猀琀爀吀愀爀最攀琀䄀瀀瀀 㴀 ∀栀琀琀瀀㨀⼀⼀㄀㤀㠀⸀㐀㔀⸀㄀㜀⸀㈀㌀㨀㠀　　　⼀猀栀漀挀欀眀愀瘀攀琀攀洀瀀氀愀琀攀猀⼀挀愀戀开愀欀开猀甀戀琀攀猀琀⸀猀眀琀∀㬀ഀഀ
            if (Application.CustomProperties.item("SWFAllItems") != null) {਍               猀琀爀吀愀爀最攀琀䄀瀀瀀 㴀 䄀瀀瀀氀椀挀愀琀椀漀渀⸀䌀甀猀琀漀洀倀爀漀瀀攀爀琀椀攀猀⸀椀琀攀洀⠀∀匀圀䘀䄀氀氀䤀琀攀洀猀∀⤀⸀嘀愀氀甀攀㬀ഀഀ
            } else {਍               䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀 ⠀∀匀圀䘀䄀氀氀䤀琀攀洀猀 椀渀 琀栀攀 砀洀挀甀猀⸀椀渀椀 昀椀氀攀 椀猀 渀漀琀 愀瘀愀椀愀戀氀攀⸀∀⤀㬀ഀഀ
               ActiveDocument.CustomDocumentProperties.add("SwitchView", ActiveDocument.PreviousViewType);਍               爀攀琀甀爀渀㬀ഀഀ
            }਍            ⼀⼀ 䜀攀琀 愀爀攀愀嘀倀愀琀栀 昀漀爀 椀洀愀最攀猀ഀഀ
            var doc=Application.ActiveDocument;਍            瘀愀爀 愀爀攀愀倀愀琀栀 㴀 渀甀氀氀㬀ഀഀ
            areaPath=GetAttrValue(doc,"interwoven","areaVPath");਍            椀昀 ⠀愀爀攀愀倀愀琀栀 㴀㴀 渀甀氀氀⤀ 笀ഀഀ
               areaPath =  getAreaVPathFromLaunchPad(); // Get VPath from local filesystem.਍            紀ഀഀ
            if( areaPath == null ) {਍               䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀 ⠀∀䔀爀爀漀爀 椀渀 ⠀∀ ⬀ 猀琀爀䘀甀渀挀琀椀漀渀一愀洀攀 ⬀ ∀⤀Ⰰ 唀渀愀戀氀攀 琀漀 最攀琀 嘀倀愀琀栀 昀漀爀 椀洀愀最攀猀⸀∀⤀㬀ഀഀ
               ActiveDocument.CustomDocumentProperties.add("SwitchView", ActiveDocument.PreviousViewType);਍                爀攀琀甀爀渀 ⴀ㄀㬀ഀഀ
            }਍            猀琀爀匀圀䘀䘀椀氀攀渀愀洀攀 㴀  䄀瀀瀀氀椀挀愀琀椀漀渀⸀倀愀琀栀 ⬀ ∀尀尀匀愀洀瀀氀攀猀尀尀䌀吀䈀尀尀刀㈀开愀氀氀椀琀攀洀猀⸀砀洀氀∀ഀഀ
਍            瘀愀爀 爀渀最 㴀 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀刀愀渀最攀㬀ഀഀ
            if (rng.IsParentElement ("SubTest")) {਍               眀栀椀氀攀 ⠀爀渀最⸀䌀漀渀琀愀椀渀攀爀一漀搀攀⸀渀漀搀攀一愀洀攀 ℀㴀 ∀匀甀戀吀攀猀琀∀⤀ 笀ഀഀ
                  rng.SelectElement();਍               紀ഀഀ
               rng.SelectElement();਍               琀爀礀 笀ഀഀ
                  objStream = objFSO.CreateTextFile( strSWFFilename, true, false );਍                  漀戀樀匀琀爀攀愀洀⸀圀爀椀琀攀⠀∀㰀㼀砀洀氀 瘀攀爀猀椀漀渀㴀尀∀㄀⸀　尀∀ 攀渀挀漀搀椀渀最㴀尀∀唀吀䘀ⴀ㠀尀∀ 猀琀愀渀搀愀氀漀渀攀㴀尀∀渀漀尀∀㼀㸀㰀℀䐀伀䌀吀夀倀䔀 匀甀戀吀攀猀琀 匀夀匀吀䔀䴀 ∀⤀㬀ഀഀ
                  objStream.Write ("\"" + strDoctypeDTD + "\"" + ">");਍                  猀琀爀刀渀最吀攀砀琀 㴀 爀渀最⸀琀攀砀琀㬀ഀഀ
                  strRngText = strRngText.replace (/iwts:/g, areaPath);਍                  猀琀爀刀渀最吀攀砀琀 㴀 猀琀爀刀渀最吀攀砀琀⸀爀攀瀀氀愀挀攀 ⠀⼀尀琀簀尀爀簀尀渀⼀最Ⰰ ∀∀⤀㬀ഀഀ
                  strRngText = strRngText.replace (/\&nbsp;/g, " ");਍                  猀琀爀刀渀最吀攀砀琀 㴀 猀琀爀刀渀最吀攀砀琀⸀爀攀瀀氀愀挀攀 ⠀⼀尀☀唀匀瀀愀挀攀㬀⼀最Ⰰ ∀ ∀⤀㬀ഀഀ
                  objStream.Write(strRngText);਍                  漀戀樀匀琀爀攀愀洀⸀䌀氀漀猀攀 ⠀⤀㬀ഀഀ
                  objStream = null;਍               紀 挀愀琀挀栀 ⠀攀⤀ 笀ഀഀ
                  Application.Alert ("Error in (" + strFunctionName + "), Unable to create file for Flash Preview.\n\nFile may contains Unicode Characters that is not supported in Flash.");਍⼀⼀                  䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀 ⠀∀䔀爀爀漀爀 椀渀 ⠀∀ ⬀ 猀琀爀䘀甀渀挀琀椀漀渀一愀洀攀 ⬀ ∀⤀Ⰰ 唀渀愀戀氀攀 琀漀 挀爀攀愀琀攀 堀䴀䰀 椀渀猀琀愀渀挀攀 昀漀爀 䘀氀愀猀栀 倀爀攀瘀椀攀眀⸀∀⤀㬀ഀഀ
                  ActiveDocument.CustomDocumentProperties.add("SwitchView", ActiveDocument.PreviousViewType);਍                  漀戀樀匀琀爀攀愀洀 㴀 渀甀氀氀㬀ഀഀ
                  return;਍               紀ഀഀ
            } else if (rng.IsParentElement("ItemSet")) {਍               眀栀椀氀攀 ⠀爀渀最⸀䌀漀渀琀愀椀渀攀爀一漀搀攀⸀渀漀搀攀一愀洀攀 ℀㴀 ∀䤀琀攀洀匀攀琀∀⤀ 笀ഀഀ
                  rng.SelectElement();਍               紀ഀഀ
               rng.SelectElement();਍               琀爀礀 笀ഀഀ
                  objStream = objFSO.CreateTextFile( strSWFFilename, true, false );਍                  漀戀樀匀琀爀攀愀洀⸀圀爀椀琀攀⠀∀㰀㼀砀洀氀 瘀攀爀猀椀漀渀㴀尀∀㄀⸀　尀∀ 攀渀挀漀搀椀渀最㴀尀∀唀吀䘀ⴀ㠀尀∀ 猀琀愀渀搀愀氀漀渀攀㴀尀∀渀漀尀∀㼀㸀㰀℀䐀伀䌀吀夀倀䔀 匀甀戀吀攀猀琀 匀夀匀吀䔀䴀 ∀⤀㬀ഀഀ
                  objStream.Write ("\"" + strDoctypeDTD + "\"" + "><SubTest ProductID=\"CAB\">");਍                  猀琀爀刀渀最吀攀砀琀 㴀 爀渀最⸀琀攀砀琀㬀ഀഀ
                  strRngText = strRngText.replace (/iwts:/g, areaPath);਍                  猀琀爀刀渀最吀攀砀琀 㴀 猀琀爀刀渀最吀攀砀琀⸀爀攀瀀氀愀挀攀 ⠀⼀尀琀簀尀爀簀尀渀⼀最Ⰰ ∀∀⤀㬀ഀഀ
                  strRngText = strRngText.replace (/\&nbsp;/g, " ");਍                  猀琀爀刀渀最吀攀砀琀 㴀 猀琀爀刀渀最吀攀砀琀⸀爀攀瀀氀愀挀攀 ⠀⼀尀☀唀匀瀀愀挀攀㬀⼀最Ⰰ ∀ ∀⤀㬀ഀഀ
                  objStream.Write(strRngText);਍                  漀戀樀匀琀爀攀愀洀⸀圀爀椀琀攀⠀∀㰀⼀匀甀戀吀攀猀琀㸀∀⤀㬀ഀഀ
                  objStream.Close ();਍                  漀戀樀匀琀爀攀愀洀 㴀 渀甀氀氀㬀ഀഀ
               } catch (e) {਍                  䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀 ⠀∀䔀爀爀漀爀 椀渀 ⠀∀ ⬀ 猀琀爀䘀甀渀挀琀椀漀渀一愀洀攀 ⬀ ∀⤀Ⰰ 唀渀愀戀氀攀 琀漀 挀爀攀愀琀攀 昀椀氀攀 昀漀爀 䘀氀愀猀栀 倀爀攀瘀椀攀眀⸀尀渀尀渀䘀椀氀攀 洀愀礀 挀漀渀琀愀椀渀猀 唀渀椀挀漀搀攀 䌀栀愀爀愀挀琀攀爀猀 琀栀愀琀 椀猀 渀漀琀 猀甀瀀瀀漀爀琀攀搀 椀渀 䘀氀愀猀栀⸀∀⤀㬀ഀഀ
//                  Application.Alert ("Error in (" + strFunctionName + "), Unable to create XML instance for Flash Preview.");਍                  䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀䌀甀猀琀漀洀䐀漀挀甀洀攀渀琀倀爀漀瀀攀爀琀椀攀猀⸀愀搀搀⠀∀匀眀椀琀挀栀嘀椀攀眀∀Ⰰ 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀倀爀攀瘀椀漀甀猀嘀椀攀眀吀礀瀀攀⤀㬀ഀഀ
                  objStream = null;਍                  爀攀琀甀爀渀㬀ഀഀ
               }਍            紀 攀氀猀攀 椀昀 ⠀爀渀最⸀䤀猀倀愀爀攀渀琀䔀氀攀洀攀渀琀 ⠀∀䤀琀攀洀∀⤀⤀ 笀ഀഀ
               while (rng.ContainerNode.nodeName != "Item") {਍                  爀渀最⸀匀攀氀攀挀琀䔀氀攀洀攀渀琀⠀⤀㬀ഀഀ
               }਍               爀渀最⸀匀攀氀攀挀琀䔀氀攀洀攀渀琀⠀⤀㬀ഀഀ
               try {਍                  漀戀樀匀琀爀攀愀洀 㴀 漀戀樀䘀匀伀⸀䌀爀攀愀琀攀吀攀砀琀䘀椀氀攀⠀ 猀琀爀匀圀䘀䘀椀氀攀渀愀洀攀Ⰰ 琀爀甀攀Ⰰ 昀愀氀猀攀 ⤀㬀ഀഀ
                  objStream.Write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE SubTest SYSTEM ");਍                  漀戀樀匀琀爀攀愀洀⸀圀爀椀琀攀 ⠀∀尀∀∀ ⬀ 猀琀爀䐀漀挀琀礀瀀攀䐀吀䐀 ⬀ ∀尀∀∀ ⬀ ∀㸀㰀匀甀戀吀攀猀琀 倀爀漀搀甀挀琀䤀䐀㴀尀∀䌀䄀䈀尀∀㸀㰀䤀琀攀洀匀攀琀 䤀䐀㴀尀∀㄀尀∀㸀∀⤀㬀ഀഀ
                  strRngText = rng.text;਍                  猀琀爀刀渀最吀攀砀琀 㴀 猀琀爀刀渀最吀攀砀琀⸀爀攀瀀氀愀挀攀 ⠀⼀椀眀琀猀㨀⼀最Ⰰ 愀爀攀愀倀愀琀栀⤀㬀ഀഀ
                  strRngText = strRngText.replace (/\t|\r|\n/g, "");਍                  猀琀爀刀渀最吀攀砀琀 㴀 猀琀爀刀渀最吀攀砀琀⸀爀攀瀀氀愀挀攀 ⠀⼀尀☀渀戀猀瀀㬀⼀最Ⰰ ∀ ∀⤀㬀ഀഀ
                  strRngText = strRngText.replace (/\&USpace;/g, " ");਍                  漀戀樀匀琀爀攀愀洀⸀圀爀椀琀攀⠀猀琀爀刀渀最吀攀砀琀⤀㬀ഀഀ
                  objStream.Write("</ItemSet></SubTest>");਍                  漀戀樀匀琀爀攀愀洀⸀䌀氀漀猀攀 ⠀⤀㬀ഀഀ
                  objStream = null;਍               紀 挀愀琀挀栀 ⠀攀⤀ 笀ഀഀ
                  Application.Alert ("Error in (" + strFunctionName + "), Unable to create file for Flash Preview.\n\nFile may contains Unicode Characters that is not supported in Flash.");਍⼀⼀                  䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀 ⠀∀䔀爀爀漀爀 椀渀 ⠀∀ ⬀ 猀琀爀䘀甀渀挀琀椀漀渀一愀洀攀 ⬀ ∀⤀Ⰰ 唀渀愀戀氀攀 琀漀 挀爀攀愀琀攀 堀䴀䰀 椀渀猀琀愀渀挀攀 昀漀爀 䘀氀愀猀栀 倀爀攀瘀椀攀眀⸀∀⤀㬀ഀഀ
                  ActiveDocument.CustomDocumentProperties.add("SwitchView", ActiveDocument.PreviousViewType);਍                  漀戀樀匀琀爀攀愀洀 㴀 渀甀氀氀㬀ഀഀ
                  return;਍               紀ഀഀ
            } else {਍               䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀 ⠀∀倀氀攀愀猀攀 瀀漀猀椀琀椀漀渀 礀漀甀爀 挀甀爀猀漀爀 椀渀猀椀搀攀 漀昀 愀渀 匀甀戀吀攀猀琀Ⰰ 䤀琀攀洀匀攀琀Ⰰ 漀爀 䤀琀攀洀 昀漀爀 䘀氀愀猀栀 倀爀攀瘀椀攀眀⸀∀⤀㬀ഀഀ
               ActiveDocument.CustomDocumentProperties.add("SwitchView", ActiveDocument.PreviousViewType);਍               爀攀琀甀爀渀㬀ഀഀ
            }਍ഀഀ
            // Uploading SWF file to JGen for Flash Preview.਍            琀爀礀 笀ഀഀ
               if (intReturnValue == 3) {਍                  瘀愀爀 䈀爀嘀椀攀眀 㴀 渀攀眀 䄀挀琀椀瘀攀堀伀戀樀攀挀琀 ⠀∀猀眀昀甀瀀氀漀愀搀⸀唀瀀氀漀愀搀䌀氀愀猀猀㄀∀⤀㬀ഀഀ
               } else if (intReturnValue == 6) {    // Preview in Separate Windows.਍                  瘀愀爀 䈀爀嘀椀攀眀 㴀 渀攀眀 䄀挀琀椀瘀攀堀伀戀樀攀挀琀 ⠀∀猀眀昀甀瀀氀漀愀搀䈀爀漀眀猀攀爀⸀唀瀀氀漀愀搀䌀氀愀猀猀㄀∀⤀㬀ഀഀ
               }਍                  ⼀⼀䈀爀嘀椀攀眀⸀唀瀀氀漀愀搀䘀椀氀攀吀漀䘀伀倀⠀⤀㬀ഀഀ
               Application.SetCursor(2);਍               䄀瀀瀀氀椀挀愀琀椀漀渀⸀匀攀琀匀琀愀琀甀猀吀攀砀琀 ⠀∀䘀椀氀攀 戀攀椀渀最 琀爀愀渀猀昀漀爀洀攀搀 昀漀爀 䘀氀愀猀栀⸀  吀栀椀猀 洀愀礀 琀愀欀攀 猀攀瘀攀爀愀氀 猀攀挀漀渀搀猀⸀  倀氀攀愀猀攀 眀愀椀琀 ⸀ ⸀ ⸀ ⸀ ⸀∀⤀㬀ഀഀ
               BrView.UploadFileToFOP(strSWFURL, strSWFFilename, strPreviewFilename, strTargetApp, strTargetField, SWFTimeOut);਍               椀昀 ⠀椀渀琀刀攀琀甀爀渀嘀愀氀甀攀 㴀㴀 㘀⤀ 笀    ⼀⼀ 倀爀攀瘀椀攀眀 椀渀 匀攀瀀愀爀愀琀攀 圀椀渀搀漀眀猀⸀ഀഀ
                  ActiveDocument.CustomDocumentProperties.add("SwitchView", sqViewBrowse);਍               紀ഀഀ
            } catch (e) {਍               䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀 ⠀∀䔀爀爀漀爀 椀渀 ⠀∀ ⬀ 猀琀爀䘀甀渀挀琀椀漀渀一愀洀攀 ⬀ ∀⤀Ⰰ 唀渀愀戀氀攀 琀漀 氀漀愀搀 匀圀䘀唀瀀氀漀愀搀⸀搀氀氀㨀 ∀ ⬀ 攀⸀搀攀猀挀爀椀瀀琀椀漀渀⤀㬀ഀഀ
               ActiveDocument.CustomDocumentProperties.add("SwitchView", ActiveDocument.PreviousViewType);਍               爀攀琀甀爀渀㬀ഀഀ
            }਍            䄀瀀瀀氀椀挀愀琀椀漀渀⸀匀攀琀匀琀愀琀甀猀吀攀砀琀 ⠀∀∀⤀㬀ഀഀ
            Application.SetCursor(0);਍            琀爀礀 笀ഀഀ
               ActiveDocument.BrowserURL = Application.PathToURL( strPreviewFilename );਍            紀 挀愀琀挀栀 ⠀攀⤀ 笀紀ഀഀ
         }਍      紀ഀഀ
   } else {਍      ⼀⼀ 䌀愀渀挀攀氀椀渀最 瘀椀攀眀⸀ഀഀ
      ActiveDocument.CustomDocumentProperties.add("SwitchView", ActiveDocument.PreviousViewType);਍   紀ഀഀ
   objdlg = null;਍   漀戀樀䘀猀漀 㴀 渀甀氀氀㬀ഀഀ
}਍最攀琀倀爀攀瘀椀攀眀吀礀瀀攀 ⠀⤀㬀ഀഀ
਍崀崀㸀㰀⼀䴀䄀䌀刀伀㸀ഀഀ
਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀吀漀最最氀攀 䌀漀洀洀攀渀琀猀∀ 栀椀搀攀㴀∀琀爀甀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㄀㌀㜀㈀∀㸀ഀഀ
<![CDATA[਍昀甀渀挀琀椀漀渀 吀漀最最氀攀䌀漀洀洀攀渀琀猀 ⠀⤀ 笀ഀഀ
   var rng = ActiveDocument.Range;਍   瘀愀爀 爀渀最㈀ 㴀 爀渀最⸀䐀甀瀀氀椀挀愀琀攀㬀ഀഀ
   if (! rng2.IsParentElement ("Comments")) {਍      爀渀最㈀⸀䴀漀瘀攀刀椀最栀琀⠀猀焀䴀漀瘀攀⤀㬀ഀഀ
   }਍   椀昀 ⠀℀ 爀渀最㈀⸀䤀猀倀愀爀攀渀琀䔀氀攀洀攀渀琀 ⠀∀䌀漀洀洀攀渀琀猀∀⤀⤀ 笀ഀഀ
      rng2 = rng;਍      爀渀最㈀⸀䴀漀瘀攀䰀攀昀琀⠀猀焀䴀漀瘀攀⤀㬀ഀഀ
   }਍   椀昀 ⠀℀ 爀渀最㈀⸀䤀猀倀愀爀攀渀琀䔀氀攀洀攀渀琀 ⠀∀䌀漀洀洀攀渀琀猀∀⤀⤀ 笀ഀഀ
      rng2 = rng;਍   紀 攀氀猀攀 笀ഀഀ
      rng = rng2;਍   紀ഀഀ
   if (rng.IsParentElement("Comments")) {਍      眀栀椀氀攀 ⠀爀渀最⸀䌀漀渀琀愀椀渀攀爀一漀搀攀⸀渀漀搀攀一愀洀攀 ℀㴀 ∀䌀漀洀洀攀渀琀猀∀⤀ 笀ഀഀ
         if (rng.ContainerNode.nodeName == "Item") {਍            爀渀最㈀⸀匀攀氀攀挀琀⠀⤀㬀ഀഀ
            return; ਍         紀ഀഀ
         rng.SelectElement();਍      紀ഀഀ
      rng.Select();਍      椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀漀氀氀愀瀀猀攀搀䌀漀渀琀愀椀渀攀爀吀愀最猀⤀ 笀ഀഀ
         // Expand tags਍         匀攀氀攀挀琀椀漀渀⸀䌀漀氀氀愀瀀猀攀搀䌀漀渀琀愀椀渀攀爀吀愀最猀 㴀 昀愀氀猀攀㬀ഀഀ
      } else {਍         ⼀⼀ 䌀漀氀氀愀瀀猀攀 琀愀最猀ഀഀ
         Selection.CollapsedContainerTags = true;਍      紀ഀഀ
   }਍紀ഀഀ
ToggleComments ();਍崀崀㸀ഀഀ
</MACRO>਍ഀഀ
<MACRO name="Expand All Comments" hide="true" lang="JScript" id="1373">਍㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
function ExpandAllComments () {਍   瘀愀爀 爀渀最 㴀 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀刀愀渀最攀㬀ഀഀ
   var rng2 = rng.Duplicate;਍   瘀愀爀 渀漀搀䰀椀猀琀 㴀 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀最攀琀䔀氀攀洀攀渀琀猀䈀礀吀愀最一愀洀攀⠀∀䌀漀洀洀攀渀琀猀∀⤀㬀 ഀഀ
   for (var i=0; i < nodList.Length; i++) {਍      爀渀最㈀⸀匀攀氀攀挀琀一漀搀攀䌀漀渀琀攀渀琀猀⠀渀漀搀䰀椀猀琀⸀椀琀攀洀⠀椀⤀⤀㬀   ഀഀ
//      if (rng2.CollapsedContainerTags) {਍⼀⼀         ⼀⼀ 䔀砀瀀愀渀搀 琀愀最猀ഀഀ
         rng2.CollapsedContainerTags = false;਍⼀⼀      紀 攀氀猀攀 笀ഀഀ
//         // Collapse tags਍⼀⼀         爀渀最㈀⸀䌀漀氀氀愀瀀猀攀搀䌀漀渀琀愀椀渀攀爀吀愀最猀 㴀 琀爀甀攀㬀ഀഀ
//      }਍   紀 ഀഀ
   rng.Select();਍紀ഀഀ
ExpandAllComments ();਍崀崀㸀ഀഀ
</MACRO>਍ഀഀ
<MACRO name="Collapse All Comments" hide="true" lang="JScript" id="1374">਍㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
function CollapseAllComments () {਍   瘀愀爀 爀渀最 㴀 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀刀愀渀最攀㬀ഀഀ
   var rng2 = rng.Duplicate;਍   瘀愀爀 渀漀搀䰀椀猀琀 㴀 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀最攀琀䔀氀攀洀攀渀琀猀䈀礀吀愀最一愀洀攀⠀∀䌀漀洀洀攀渀琀猀∀⤀㬀 ഀഀ
   for (var i=0; i < nodList.Length; i++) {਍      爀渀最㈀⸀匀攀氀攀挀琀一漀搀攀䌀漀渀琀攀渀琀猀⠀渀漀搀䰀椀猀琀⸀椀琀攀洀⠀椀⤀⤀㬀   ഀഀ
//      if (rng2.CollapsedContainerTags) {਍⼀⼀         ⼀⼀ 䔀砀瀀愀渀搀 琀愀最猀ഀഀ
//         rng2.CollapsedContainerTags = false;਍⼀⼀      紀 攀氀猀攀 笀ഀഀ
//         // Collapse tags਍         爀渀最㈀⸀䌀漀氀氀愀瀀猀攀搀䌀漀渀琀愀椀渀攀爀吀愀最猀 㴀 琀爀甀攀㬀ഀഀ
//      }਍   紀 ഀഀ
   rng.Select();਍紀ഀഀ
CollapseAllComments ();਍崀崀㸀ഀഀ
</MACRO>਍ഀഀ
<MACRO name="Toggle Hierarchy" hide="true" lang="JScript" id="1054">਍㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
function ToggleHierarchy () {਍   瘀愀爀 爀渀最 㴀 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀刀愀渀最攀㬀ഀഀ
   var rng2 = rng.Duplicate;਍   椀昀 ⠀℀ 爀渀最㈀⸀䤀猀倀愀爀攀渀琀䔀氀攀洀攀渀琀 ⠀∀䠀椀攀爀愀爀挀栀礀∀⤀⤀ 笀ഀഀ
      rng2.MoveRight(sqMove);਍   紀ഀഀ
   if (! rng2.IsParentElement ("Hierarchy")) {਍      爀渀最㈀ 㴀 爀渀最㬀ഀഀ
      rng2.MoveLeft(sqMove);਍   紀ഀഀ
   if (! rng2.IsParentElement ("Hierarchy")) {਍      爀渀最㈀ 㴀 爀渀最㬀ഀഀ
   } else {਍      爀渀最 㴀 爀渀最㈀㬀ഀഀ
   }਍   椀昀 ⠀爀渀最⸀䤀猀倀愀爀攀渀琀䔀氀攀洀攀渀琀⠀∀䠀椀攀爀愀爀挀栀礀∀⤀⤀ 笀ഀഀ
      while (rng.ContainerNode.nodeName == "Hierarchy") {਍         爀渀最⸀匀攀氀攀挀琀䔀氀攀洀攀渀琀⠀⤀㬀ഀഀ
      }਍      爀渀最⸀匀攀氀攀挀琀⠀⤀㬀ഀഀ
      Selection.Collapse(sqCollapseStart);਍      匀攀氀攀挀琀椀漀渀⸀䴀漀瘀攀刀椀最栀琀⠀猀焀䴀漀瘀攀⤀㬀ഀഀ
      if (Selection.CollapsedContainerTags) {਍         ⼀⼀ 䔀砀瀀愀渀搀 琀愀最猀ഀഀ
         Selection.CollapsedContainerTags = false;਍      紀 攀氀猀攀 笀ഀഀ
         // Collapse tags਍         匀攀氀攀挀琀椀漀渀⸀䌀漀氀氀愀瀀猀攀搀䌀漀渀琀愀椀渀攀爀吀愀最猀 㴀 琀爀甀攀㬀ഀഀ
      }਍   紀ഀഀ
}਍吀漀最最氀攀䠀椀攀爀愀爀挀栀礀 ⠀⤀㬀ഀഀ
]]>਍㰀⼀䴀䄀䌀刀伀㸀ഀഀ
਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀䌀漀氀氀愀瀀猀攀 䄀氀氀 䠀椀攀爀愀爀挀栀礀∀ 栀椀搀攀㴀∀琀爀甀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㄀㔀㈀㜀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
਍ഀഀ
function CollapseAllHierarchy () {਍ഀഀ
   var rng = ActiveDocument.Range;਍   瘀愀爀 爀渀最㈀ 㴀 爀渀最⸀䐀甀瀀氀椀挀愀琀攀㬀ഀഀ
   var rng3 = rng.Duplicate;਍   瘀愀爀 渀漀搀䰀椀猀琀 㴀 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀最攀琀䔀氀攀洀攀渀琀猀䈀礀吀愀最一愀洀攀⠀∀䠀椀攀爀愀爀挀栀礀∀⤀㬀 ഀഀ
   var i=0;਍   眀栀椀氀攀 ⠀椀 㰀 渀漀搀䰀椀猀琀⸀䰀攀渀最琀栀⤀ 笀ഀഀ
      rng2.SelectNodeContents(nodList.item(i));਍      爀渀最㌀ 㴀 爀渀最㈀⸀䐀甀瀀氀椀挀愀琀攀㬀ഀഀ
      i = i + 1;਍      椀昀 ⠀椀 㰀 渀漀搀䰀椀猀琀⸀䰀攀渀最琀栀⤀ 笀ഀഀ
         rng3.SelectNodeContents(nodList.item(i));਍         眀栀椀氀攀 ⠀⠀爀渀最㈀⸀䌀漀渀琀愀椀渀猀⠀爀渀最㌀⤀⤀ ☀☀ ⠀椀 㰀 渀漀搀䰀椀猀琀⸀䰀攀渀最琀栀 ⴀ ㄀⤀⤀ 笀ഀഀ
            i = i + 1;਍            爀渀最㌀⸀匀攀氀攀挀琀一漀搀攀䌀漀渀琀攀渀琀猀⠀渀漀搀䰀椀猀琀⸀椀琀攀洀⠀椀⤀⤀㬀ഀഀ
         }਍      紀ഀഀ
//      if (rng2.CollapsedContainerTags) {਍⼀⼀         ⼀⼀ 䔀砀瀀愀渀搀 琀愀最猀ഀഀ
//         rng2.CollapsedContainerTags = false;਍⼀⼀      紀 攀氀猀攀 笀ഀഀ
//         // Collapse tags਍         爀渀最㈀⸀䌀漀氀氀愀瀀猀攀搀䌀漀渀琀愀椀渀攀爀吀愀最猀 㴀 琀爀甀攀㬀ഀഀ
//      }਍   紀 ഀഀ
   rng.Select();਍紀ഀഀ
CollapseAllHierarchy ();਍崀崀㸀㰀⼀䴀䄀䌀刀伀㸀ഀഀ
਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀䔀砀瀀愀渀搀 䄀氀氀 䠀椀攀爀愀爀挀栀礀∀ 栀椀搀攀㴀∀琀爀甀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㄀㔀㈀㠀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
਍ഀഀ
function ExpandAllHierarchy () {਍ഀഀ
   var rng = ActiveDocument.Range;਍   瘀愀爀 爀渀最㈀ 㴀 爀渀最⸀䐀甀瀀氀椀挀愀琀攀㬀ഀഀ
   var rng3 = rng.Duplicate;਍   瘀愀爀 渀漀搀䰀椀猀琀 㴀 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀最攀琀䔀氀攀洀攀渀琀猀䈀礀吀愀最一愀洀攀⠀∀䠀椀攀爀愀爀挀栀礀∀⤀㬀 ഀഀ
   var i=0;਍   眀栀椀氀攀 ⠀椀 㰀 渀漀搀䰀椀猀琀⸀䰀攀渀最琀栀⤀ 笀ഀഀ
      rng2.SelectNodeContents(nodList.item(i));਍      爀渀最㌀ 㴀 爀渀最㈀⸀䐀甀瀀氀椀挀愀琀攀㬀ഀഀ
      if (rng2.CollapsedContainerTags) {਍         ⼀⼀ 䔀砀瀀愀渀搀 琀愀最猀ഀഀ
         rng2.CollapsedContainerTags = false;਍      紀ഀഀ
      i = i + 1;਍   紀 ഀഀ
   rng.Select();਍紀ഀഀ
ExpandAllHierarchy ();਍崀崀㸀㰀⼀䴀䄀䌀刀伀㸀ഀഀ
਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀䤀渀猀攀爀琀 一攀眀 䰀椀猀琀 䤀琀攀洀∀ 栀椀搀攀㴀∀琀爀甀攀∀ 氀愀渀最㴀∀嘀䈀匀挀爀椀瀀琀∀㸀ഀഀ
<!--਍✀ ⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀ഀഀ
' DESCRIPTION਍✀ 吀栀椀猀 洀愀挀爀漀 椀猀 愀 氀椀戀爀愀爀礀 洀愀挀爀漀⸀  䤀琀 挀漀渀琀愀椀渀猀 昀甀渀挀琀椀漀渀猀 昀漀爀ഀഀ
' manipulating lists.਍✀ഀഀ
' HISTORY਍✀ ㄀⸀ ㈀　　㄀　㤀㈀㄀ 匀漀昀琀儀甀愀搀㨀 刀漀攀栀氀 匀椀漀猀漀渀ഀഀ
'    Official Release਍✀ ⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀ഀഀ
-->਍㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
਍✀ ⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀ഀഀ
' Sub InsertNewListItem()਍✀ ⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀ഀഀ
' DESCRIPTION਍✀ 䤀渀猀攀爀琀猀 愀 渀攀眀 氀椀猀琀椀琀攀洀 愀琀 琀栀攀 椀渀猀攀爀琀椀漀渀 瀀漀椀渀琀⸀  䤀昀 琀栀攀ഀഀ
' insertion point is currently inside a listitem, the਍✀ 椀渀猀攀爀琀椀漀渀 瀀漀椀渀琀 椀猀 洀漀瘀攀搀 琀漀 愀昀琀攀爀 琀栀攀 挀甀爀爀攀渀琀 氀椀猀琀椀琀攀洀⸀ഀഀ
' Function is disabled when a list element is not an ancestor.਍✀ഀഀ
' PARAMETERS਍✀ 一漀渀攀ഀഀ
'਍✀ 刀䔀吀唀刀一 嘀䄀䰀唀䔀ഀഀ
' None਍✀ഀഀ
' NOTES਍✀ ㄀⸀ 倀爀攀ⴀ爀攀氀攀愀猀攀㨀 ∀刀攀愀搀ⴀ伀渀氀礀 䐀漀挀甀洀攀渀琀 䠀愀渀搀氀攀爀∀ഀഀ
'    This function is not designed to work on a read-only਍✀    搀漀挀甀洀攀渀琀⸀  吀栀攀 栀愀渀搀氀攀爀 眀椀氀氀 昀愀椀氀 眀椀琀栀 愀渀 愀氀攀爀琀⸀ഀഀ
'਍✀ ㈀⸀ 倀爀攀ⴀ爀攀氀攀愀猀攀㨀 ∀刀攀愀搀ⴀ伀渀氀礀 匀攀氀攀挀琀椀漀渀 䠀愀渀搀氀攀爀∀ഀഀ
'    This function is not designed to work on a read-only਍✀    猀攀氀攀挀琀椀漀渀⸀  吀栀攀 栀愀渀搀氀攀爀 眀椀氀氀 昀愀椀氀 眀椀琀栀 愀渀 愀氀攀爀琀⸀ഀഀ
'਍✀ ㌀⸀ 倀爀攀ⴀ爀攀氀攀愀猀攀㨀 ∀倀氀愀椀渀 吀攀砀琀 䠀愀渀搀氀攀爀∀ഀഀ
'    This function is not designed to work on document in਍✀    倀氀愀椀渀 吀攀砀琀 洀漀搀攀⸀  吀栀攀 栀愀渀搀氀攀爀 眀椀氀氀 昀愀椀氀 眀椀琀栀 愀渀 愀氀攀爀琀⸀ഀഀ
'਍✀ 㐀⸀ 倀爀攀ⴀ爀攀氀攀愀猀攀㨀 ∀䤀渀 䐀漀挀甀洀攀渀琀 䈀漀搀礀 䠀愀渀搀氀攀爀∀ഀഀ
'    This function is not designed to work outside of the਍✀    搀漀挀甀洀攀渀琀 戀漀搀礀⸀  吀栀攀 栀愀渀搀氀攀爀 眀椀氀氀 昀愀椀氀 眀椀琀栀 愀渀 愀氀攀爀琀⸀ഀഀ
'਍✀ 㔀⸀ 倀爀攀ⴀ爀攀氀攀愀猀攀㨀 ∀嘀愀氀椀搀愀琀椀漀渀 伀昀昀 䠀愀渀搀氀攀爀∀ഀഀ
'    This function is not designed to work on non-validating਍✀    搀漀挀甀洀攀渀琀猀⸀  吀栀攀 栀愀渀搀氀攀爀 眀椀氀氀 昀愀椀氀 眀椀琀栀 愀渀 愀氀攀爀琀⸀ഀഀ
'਍✀ 䠀䤀匀吀伀刀夀ഀഀ
' 1. 20011114 SoftQuad: Roehl Sioson਍✀    ⠀刀䄀䤀䐀 㘀㈀㜀⤀ 䄀甀最甀猀琀 㠀 ㈀　　㄀ 䌀漀渀昀攀爀攀渀挀攀 挀愀氀氀 眀椀琀栀 䐀愀瘀椀搀 䠀漀爀渀ഀഀ
'    (Microsoft) and Linda Avraamides (Microsoft): Request for਍✀    琀漀漀氀戀愀爀 戀甀琀琀漀渀Ⰰ 洀攀渀甀 椀琀攀洀Ⰰ 愀渀搀 栀漀琀欀攀礀 昀漀爀 椀渀猀攀爀琀椀渀最ഀഀ
'    new listitem.਍✀ ⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀⨀ഀഀ
਍匀甀戀 䤀渀猀攀爀琀一攀眀䰀椀猀琀䤀琀攀洀⠀⤀ഀഀ
਍ऀ伀渀 䔀爀爀漀爀 刀攀猀甀洀攀 一攀砀琀ഀഀ
਍ऀ䐀椀洀 猀琀爀䘀甀渀挀琀椀漀渀一愀洀攀ഀഀ
਍ऀ猀琀爀䘀甀渀挀琀椀漀渀一愀洀攀 㴀 ∀䤀渀猀攀爀琀一攀眀䰀椀猀琀䤀琀攀洀⠀⤀∀ഀഀ
਍ऀ䤀昀 ⠀ 匀攀氀攀挀琀椀漀渀⸀刀攀愀搀伀渀氀礀 㴀 吀爀甀攀 ⤀ 吀栀攀渀ഀഀ
           Application.Alert ("Cannot run operation in read-only portions of the document.")਍ऀऀ䔀砀椀琀 匀甀戀ഀഀ
	End If਍ഀഀ
	If ( ActiveDocument.ViewType = sqViewPlainText ) Then਍           䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀 ⠀∀䌀愀渀渀漀琀 爀甀渀 漀瀀攀爀愀琀椀漀渀 椀渀 倀氀愀椀渀 吀攀砀琀 瘀椀攀眀⸀∀⤀ഀഀ
		Exit Sub਍ऀ䔀渀搀 䤀昀ഀഀ
਍ऀ匀攀琀 爀渀最吀攀猀琀 㴀 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀刀愀渀最攀ഀഀ
	If (( rngTest.IsParentElement( "OrderedList" )) Or _਍            ⠀ 爀渀最吀攀猀琀⸀䤀猀倀愀爀攀渀琀䔀氀攀洀攀渀琀 ⠀∀唀渀漀爀搀攀爀攀搀䰀椀猀琀∀⤀⤀⤀ 吀栀攀渀ഀഀ
	   If ( Not rngTest.CanInsert( "ListItem" ) ) Then਍ऀऀ䤀昀 ⠀ 一漀琀 爀渀最吀攀猀琀⸀䘀椀渀搀䤀渀猀攀爀琀䰀漀挀愀琀椀漀渀⠀ ∀䰀椀猀琀䤀琀攀洀∀ ⤀ ⤀ 吀栀攀渀ഀഀ
                   Application.Alert ("Could not find an appropriate place to insert new list item.")਍ऀऀ   䔀砀椀琀 匀甀戀ഀഀ
		End If਍           䔀渀搀 䤀昀ഀഀ
	End If਍ऀ䌀愀氀氀 爀渀最吀攀猀琀⸀䤀渀猀攀爀琀䔀氀攀洀攀渀琀⠀ ∀䰀椀猀琀䤀琀攀洀∀ ⤀ഀഀ
	Call rngTest.Select਍ഀഀ
	' Garbage collection.਍ഀഀ
	Set rngTest = Nothing਍ഀഀ
	' Default error trap.਍ഀഀ
	If ( Err.Number <> 0 ) Then਍           䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀 ⠀䔀爀爀⸀䐀攀猀挀爀椀瀀琀椀漀渀⤀ഀഀ
	End If਍ഀഀ
End Sub਍䌀愀氀氀 䤀渀猀攀爀琀一攀眀䰀椀猀琀䤀琀攀洀⠀⤀ഀഀ
]]>਍㰀⼀䴀䄀䌀刀伀㸀ഀഀ
਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀䤀渀猀攀爀琀 匀琀甀搀攀渀琀 䐀椀爀攀挀琀椀漀渀猀∀ 栀椀搀攀㴀∀琀爀甀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㄀㐀㈀㔀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
function insertstudentdirections () {਍ഀഀ
   var rng = ActiveDocument.Range;਍   椀昀 ⠀爀渀最⸀䤀猀倀愀爀攀渀琀䔀氀攀洀攀渀琀⠀∀䤀琀攀洀∀⤀⤀ 笀ഀഀ
      while (rng.ContainerNode.nodeName != "Item") {਍         爀渀最⸀匀攀氀攀挀琀䔀氀攀洀攀渀琀⠀⤀㬀ഀഀ
      }਍   紀ഀഀ
   if (rng.FindInsertLocation("StudentDirections")) {਍      爀渀最⸀䤀渀猀攀爀琀䔀氀攀洀攀渀琀⠀∀匀琀甀搀攀渀琀䐀椀爀攀挀琀椀漀渀猀∀⤀㬀ഀഀ
      rng.Select();਍      爀渀最⸀倀愀猀琀攀匀琀爀椀渀最 ⠀∀㰀㼀砀洀ⴀ爀攀瀀氀愀挀攀开琀攀砀琀 笀䄀搀搀 匀琀甀搀攀渀琀 䐀椀爀攀挀琀椀漀渀猀 琀攀砀琀 栀攀爀攀紀㼀㸀∀⤀㬀ഀഀ
   } else {਍      爀渀最⸀䴀漀瘀攀吀漀䐀漀挀甀洀攀渀琀匀琀愀爀琀⠀⤀㬀ഀഀ
      if (rng.FindInsertLocation("StudentDirections")) {਍         爀渀最⸀䤀渀猀攀爀琀䔀氀攀洀攀渀琀⠀∀匀琀甀搀攀渀琀䐀椀爀攀挀琀椀漀渀猀∀⤀㬀ഀഀ
         rng.Select();਍         爀渀最⸀倀愀猀琀攀匀琀爀椀渀最 ⠀∀㰀㼀砀洀ⴀ爀攀瀀氀愀挀攀开琀攀砀琀 笀䄀搀搀 匀琀甀搀攀渀琀 䐀椀爀攀挀琀椀漀渀猀 琀攀砀琀 栀攀爀攀紀㼀㸀∀⤀㬀ഀഀ
      } else {਍         䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀 ⠀∀匀琀甀搀攀渀琀䐀椀爀攀挀琀椀漀渀猀 琀愀最 挀愀渀 渀漀琀 戀攀 椀渀猀攀爀琀攀搀 栀攀爀攀⸀∀Ⰰ ∀䌀吀䈀∀⤀㬀ഀഀ
      }਍   紀ഀഀ
}਍椀渀猀攀爀琀猀琀甀搀攀渀琀搀椀爀攀挀琀椀漀渀猀⠀⤀㬀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀䤀渀猀攀爀琀 䌀漀洀洀攀渀琀∀ 栀椀搀攀㴀∀琀爀甀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㄀㠀　　∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
਍昀甀渀挀琀椀漀渀 䤀渀猀攀爀琀䌀漀洀洀攀渀琀 ⠀⤀ 笀ഀഀ
   var rng = ActiveDocument.Range;਍   瘀愀爀 爀渀最吀攀洀瀀㬀ഀഀ
   var rngTemp2਍   瘀愀爀 渀漀搀吀攀洀瀀㄀㬀ഀഀ
   var nodTemp2;਍  ഀഀ
   // Move up to the Parent Element which "Comment" can be inserted, there're multiple places.਍   椀昀 ⠀爀渀最⸀䤀猀倀愀爀攀渀琀䔀氀攀洀攀渀琀⠀∀䌀漀洀洀攀渀琀猀∀⤀⤀ 笀ഀഀ
      while (rng.ContainerNode.nodeName != "Comments") {਍         爀渀最⸀匀攀氀攀挀琀䔀氀攀洀攀渀琀⠀⤀㬀ഀഀ
      }਍   紀 攀氀猀攀 椀昀 ⠀爀渀最⸀䤀猀倀愀爀攀渀琀䔀氀攀洀攀渀琀⠀∀䤀琀攀洀∀⤀⤀ 笀ഀഀ
      while (rng.ContainerNode.nodeName != "Item") {਍         爀渀最⸀匀攀氀攀挀琀䔀氀攀洀攀渀琀⠀⤀㬀ഀഀ
      }਍   紀 攀氀猀攀 椀昀 ⠀爀渀最⸀䤀猀倀愀爀攀渀琀䔀氀攀洀攀渀琀⠀∀䤀琀攀洀匀攀琀∀⤀⤀ 笀ഀഀ
      while (rng.ContainerNode.nodeName != "ItemSet") {਍         爀渀最⸀匀攀氀攀挀琀䔀氀攀洀攀渀琀⠀⤀㬀ഀഀ
      }਍   紀 攀氀猀攀 椀昀 ⠀爀渀最⸀䤀猀倀愀爀攀渀琀䔀氀攀洀攀渀琀⠀∀匀愀洀瀀氀攀匀攀琀∀⤀⤀ 笀ഀഀ
      while (rng.ContainerNode.nodeName != "SampleSet") {਍         爀渀最⸀匀攀氀攀挀琀䔀氀攀洀攀渀琀⠀⤀㬀ഀഀ
      }਍   紀 攀氀猀攀 椀昀 ⠀爀渀最⸀䤀猀倀愀爀攀渀琀䔀氀攀洀攀渀琀⠀∀匀甀戀吀攀猀琀∀⤀⤀ 笀ഀഀ
      while (rng.ContainerNode.nodeName != "SubTest") {਍         爀渀最⸀匀攀氀攀挀琀䔀氀攀洀攀渀琀⠀⤀㬀ഀഀ
      }਍   紀 攀氀猀攀 椀昀 ⠀爀渀最⸀䤀猀倀愀爀攀渀琀䔀氀攀洀攀渀琀⠀∀䄀猀猀攀猀猀洀攀渀琀∀⤀⤀ 笀ഀഀ
      while (rng.ContainerNode.nodeName != "Assessment") {਍         爀渀最⸀匀攀氀攀挀琀䔀氀攀洀攀渀琀⠀⤀㬀ഀഀ
      }਍   紀ഀഀ
   rng.SelectContainerContents();਍   爀渀最⸀䌀漀氀氀愀瀀猀攀⠀猀焀䌀漀氀氀愀瀀猀攀匀琀愀爀琀⤀㬀     ⼀⼀ 䌀漀氀氀愀瀀猀攀 琀漀 猀琀愀爀琀椀渀最 瀀漀椀渀琀⸀ഀഀ
   rngTemp = rng.Duplicate;਍   爀渀最吀攀洀瀀㈀ 㴀 爀渀最⸀䐀甀瀀氀椀挀愀琀攀㬀ഀഀ
   nodTemp1 = rng.ContainerNode;਍   ⼀⼀    䐀甀攀 琀漀 琀栀攀 昀愀挀琀 琀栀愀琀 ∀䌀漀洀洀攀渀琀∀ 椀猀 渀攀猀琀攀搀 愀渀搀 椀猀 椀渀猀椀搀攀 漀昀 ∀䌀漀洀洀攀渀琀猀∀Ⰰ 眀攀 栀愀瘀攀 琀漀 琀爀礀ഀഀ
   // to insert "Comment" first within the current Content Model (i.e. Comments, item, subtest),਍   ⼀⼀ 椀昀 琀栀椀猀 昀愀椀氀攀搀 琀栀攀渀 眀攀 渀攀攀搀 昀椀爀猀琀 渀攀攀搀攀搀 琀漀 琀漀 椀渀猀攀爀琀 ∀䌀漀洀洀攀渀琀猀∀ 愀渀搀 琀栀攀渀 ∀䌀漀洀洀攀渀琀∀⸀ഀഀ
   if (rng.FindInsertLocation("Comment")) {਍      爀渀最吀攀洀瀀 㴀 爀渀最⸀䐀甀瀀氀椀挀愀琀攀㬀ഀഀ
      if ((rngTemp.IsParentElement("Comments")) && (nodTemp1.nodeName == "Comments")) {਍         㬀ഀഀ
      } else if (rngTemp.IsParentElement("Item")) {਍         眀栀椀氀攀 ⠀爀渀最吀攀洀瀀⸀䌀漀渀琀愀椀渀攀爀一漀搀攀⸀渀漀搀攀一愀洀攀 ℀㴀 ∀䤀琀攀洀∀⤀ 笀ഀഀ
            rngTemp.SelectElement();਍         紀ഀഀ
      } else if (rngTemp.IsParentElement("ItemSet")) {਍         眀栀椀氀攀 ⠀爀渀最吀攀洀瀀⸀䌀漀渀琀愀椀渀攀爀一漀搀攀⸀渀漀搀攀一愀洀攀 ℀㴀 ∀䤀琀攀洀匀攀琀∀⤀ 笀ഀഀ
            rngTemp.SelectElement();਍         紀ഀഀ
      } else if (rngTemp.IsParentElement("SampleSet")) {਍         眀栀椀氀攀 ⠀爀渀最吀攀洀瀀⸀䌀漀渀琀愀椀渀攀爀一漀搀攀⸀渀漀搀攀一愀洀攀 ℀㴀 ∀匀愀洀瀀氀攀匀攀琀∀⤀ 笀ഀഀ
            rngTemp.SelectElement();਍         紀ഀഀ
      } else if (rngTemp.IsParentElement("SubTest")) {਍         眀栀椀氀攀 ⠀爀渀最吀攀洀瀀⸀䌀漀渀琀愀椀渀攀爀一漀搀攀⸀渀漀搀攀一愀洀攀 ℀㴀 ∀匀甀戀吀攀猀琀∀⤀ 笀ഀഀ
            rngTemp.SelectElement();਍         紀ഀഀ
      } else if (rngTemp.IsParentElement("Assessment")) {਍         眀栀椀氀攀 ⠀爀渀最吀攀洀瀀⸀䌀漀渀琀愀椀渀攀爀一漀搀攀⸀渀漀搀攀一愀洀攀 ℀㴀 ∀䄀猀猀攀猀猀洀攀渀琀∀⤀ 笀ഀഀ
            rngTemp.SelectElement();਍         紀ഀഀ
      }਍      爀渀最吀攀洀瀀⸀匀攀氀攀挀琀䌀漀渀琀愀椀渀攀爀䌀漀渀琀攀渀琀猀⠀⤀㬀ഀഀ
      rngTemp.Collapse(sqCollapseStart);     // Collapse to starting point.਍      渀漀搀吀攀洀瀀㈀ 㴀 爀渀最吀攀洀瀀⸀䌀漀渀琀愀椀渀攀爀一漀搀攀㬀ഀഀ
      if (nodTemp1 == nodTemp2) {    // We're still in the same Parent element that we started out with.਍         爀渀最⸀䤀渀猀攀爀琀䔀氀攀洀攀渀琀⠀∀䌀漀洀洀攀渀琀∀⤀㬀ഀഀ
         rng.ContainerAttribute("UserName") = Application.CustomProperties.item("Application.UserName").Value;਍         爀渀最⸀䌀漀渀琀愀椀渀攀爀䄀琀琀爀椀戀甀琀攀⠀∀䐀愀琀攀匀琀愀洀瀀∀⤀ 㴀 䜀攀琀䌀甀爀爀攀渀琀䐀愀琀攀⠀⤀㬀ഀഀ
         rng.Select();਍         爀渀最⸀䤀渀猀攀爀琀䔀氀攀洀攀渀琀⠀∀倀∀⤀㬀ഀഀ
         rng.Select();਍         爀渀最⸀倀愀猀琀攀匀琀爀椀渀最 ⠀∀㰀㼀砀洀ⴀ爀攀瀀氀愀挀攀开琀攀砀琀 笀䄀搀搀 䌀漀洀洀攀渀琀 琀攀砀琀 栀攀爀攀紀㼀㸀∀⤀㬀ഀഀ
      } else {  // Reset the Selection and find the insertable location for inserting "Comments".਍         爀渀最 㴀 爀渀最吀攀洀瀀㈀㬀ഀഀ
         if (rng.FindInsertLocation ("Comments")) {਍            爀渀最⸀䤀渀猀攀爀琀䔀氀攀洀攀渀琀圀椀琀栀刀攀焀甀椀爀攀搀⠀∀䌀漀洀洀攀渀琀猀∀⤀㬀ഀഀ
            rng.ContainerAttribute("UserName") = Application.CustomProperties.item("Application.UserName").Value;਍            爀渀最⸀䌀漀渀琀愀椀渀攀爀䄀琀琀爀椀戀甀琀攀⠀∀䐀愀琀攀匀琀愀洀瀀∀⤀ 㴀 䜀攀琀䌀甀爀爀攀渀琀䐀愀琀攀⠀⤀㬀ഀഀ
            rng.Select();਍            爀渀最⸀䤀渀猀攀爀琀䔀氀攀洀攀渀琀⠀∀倀∀⤀㬀ഀഀ
            rng.Select();਍            爀渀最⸀倀愀猀琀攀匀琀爀椀渀最 ⠀∀㰀㼀砀洀ⴀ爀攀瀀氀愀挀攀开琀攀砀琀 笀䄀搀搀 䌀漀洀洀攀渀琀 琀攀砀琀 栀攀爀攀紀㼀㸀∀⤀㬀ഀഀ
         } else {਍            䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀 ⠀∀䌀漀洀洀攀渀琀 琀愀最 䌀愀渀 渀漀琀 戀攀 椀渀猀攀爀琀攀搀 栀攀爀攀⸀∀⤀㬀ഀഀ
         }਍      紀ഀഀ
   } else if (rng.FindInsertLocation("Comments")) {਍      爀渀最⸀䤀渀猀攀爀琀䔀氀攀洀攀渀琀圀椀琀栀刀攀焀甀椀爀攀搀⠀∀䌀漀洀洀攀渀琀猀∀⤀ഀഀ
      rng.ContainerAttribute("UserName") = Application.CustomProperties.item("Application.UserName").Value;਍      爀渀最⸀䌀漀渀琀愀椀渀攀爀䄀琀琀爀椀戀甀琀攀⠀∀䐀愀琀攀匀琀愀洀瀀∀⤀ 㴀 䜀攀琀䌀甀爀爀攀渀琀䐀愀琀攀⠀⤀㬀ഀഀ
      rng.Select();਍      爀渀最⸀䤀渀猀攀爀琀䔀氀攀洀攀渀琀⠀∀倀∀⤀㬀ഀഀ
      rng.Select();਍      爀渀最⸀倀愀猀琀攀匀琀爀椀渀最 ⠀∀㰀㼀砀洀ⴀ爀攀瀀氀愀挀攀开琀攀砀琀 笀䄀搀搀 䌀漀洀洀攀渀琀 琀攀砀琀 栀攀爀攀紀㼀㸀∀⤀㬀ഀഀ
   } else {਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀 ⠀∀䌀漀洀洀攀渀琀 琀愀最 䌀愀渀 渀漀琀 戀攀 椀渀猀攀爀琀攀搀 栀攀爀攀⸀∀⤀㬀ഀഀ
   }਍紀ഀഀ
InsertComment ();਍崀崀㸀㰀⼀䴀䄀䌀刀伀㸀ഀഀ
<MACRO name="Insert Stimulus" hide="true" lang="JScript" id="1429"><![CDATA[਍昀甀渀挀琀椀漀渀 椀渀猀攀爀琀匀琀椀洀甀氀甀猀 ⠀⤀ 笀ഀഀ
਍   瘀愀爀 爀渀最 㴀 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀刀愀渀最攀㬀ഀഀ
   if (rng.IsParentElement("Item")) {਍      眀栀椀氀攀 ⠀爀渀最⸀䌀漀渀琀愀椀渀攀爀一漀搀攀⸀渀漀搀攀一愀洀攀 ℀㴀 ∀䤀琀攀洀∀⤀ 笀ഀഀ
         rng.SelectElement();਍      紀ഀഀ
   }਍   椀昀 ⠀爀渀最⸀䘀椀渀搀䤀渀猀攀爀琀䰀漀挀愀琀椀漀渀⠀∀匀琀椀洀甀氀甀猀∀⤀⤀ 笀ഀഀ
      rng.InsertElement("Stimulus");਍      爀渀最⸀匀攀氀攀挀琀⠀⤀㬀ഀഀ
      rng.InsertElement("Passage");਍      爀渀最⸀匀攀氀攀挀琀⠀⤀㬀ഀഀ
      rng.InsertElement("P");਍      爀渀最⸀匀攀氀攀挀琀⠀⤀㬀ഀഀ
      rng.PasteString ("<?xm-replace_text {Add Passage text here}?>");਍   紀 攀氀猀攀 笀ഀഀ
      rng.MoveToDocumentStart();਍      椀昀 ⠀爀渀最⸀䘀椀渀搀䤀渀猀攀爀琀䰀漀挀愀琀椀漀渀⠀∀匀琀椀洀甀氀甀猀∀⤀⤀ 笀ഀഀ
         rng.InsertElement("Stimulus");਍         爀渀最⸀匀攀氀攀挀琀⠀⤀㬀ഀഀ
         rng.InsertElement("Passage");਍         爀渀最⸀匀攀氀攀挀琀⠀⤀㬀ഀഀ
         rng.InsertElement("P");਍         爀渀最⸀倀愀猀琀攀匀琀爀椀渀最 ⠀∀㰀㼀砀洀ⴀ爀攀瀀氀愀挀攀开琀攀砀琀 笀䄀搀搀 倀愀猀猀愀最攀 琀攀砀琀 栀攀爀攀紀㼀㸀∀⤀㬀ഀഀ
      } else {਍         䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀 ⠀∀匀琀椀洀甀氀甀猀 琀愀最 挀愀渀 渀漀琀 戀攀 椀渀猀攀爀琀攀搀 栀攀爀攀⸀∀Ⰰ ∀䌀吀䈀∀⤀㬀ഀഀ
      }਍   紀ഀഀ
}਍椀渀猀攀爀琀匀琀椀洀甀氀甀猀⠀⤀㬀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀䤀渀猀攀爀琀 䰀攀愀搀䤀渀∀ 栀椀搀攀㴀∀琀爀甀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㄀㔀㈀㈀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
function insertLeadIn () {਍ഀഀ
   var rng = ActiveDocument.Range;਍   椀昀 ⠀爀渀最⸀䤀猀倀愀爀攀渀琀䔀氀攀洀攀渀琀⠀∀䤀琀攀洀∀⤀⤀ 笀ഀഀ
      while (rng.ContainerNode.nodeName != "Item") {਍         爀渀最⸀匀攀氀攀挀琀䔀氀攀洀攀渀琀⠀⤀㬀ഀഀ
      }਍   紀ഀഀ
   if (rng.FindInsertLocation("LeadIn")) {਍      爀渀最⸀䤀渀猀攀爀琀䔀氀攀洀攀渀琀⠀∀䰀攀愀搀䤀渀∀⤀㬀ഀഀ
      rng.Select();਍      爀渀最⸀䤀渀猀攀爀琀䔀氀攀洀攀渀琀⠀∀倀∀⤀㬀ഀഀ
      rng.Select();਍      爀渀最⸀倀愀猀琀攀匀琀爀椀渀最 ⠀∀㰀㼀砀洀ⴀ爀攀瀀氀愀挀攀开琀攀砀琀 笀䄀搀搀 䰀攀愀搀䤀渀 琀攀砀琀 栀攀爀攀紀㼀㸀∀⤀㬀ഀഀ
   } else {਍      爀渀最⸀䴀漀瘀攀吀漀䐀漀挀甀洀攀渀琀匀琀愀爀琀⠀⤀㬀ഀഀ
      if (rng.FindInsertLocation("LeadIn")) {਍         爀渀最⸀䤀渀猀攀爀琀䔀氀攀洀攀渀琀⠀∀䰀攀愀搀䤀渀∀⤀㬀ഀഀ
         rng.Select();਍         爀渀最⸀䤀渀猀攀爀琀䔀氀攀洀攀渀琀⠀∀倀∀⤀㬀ഀഀ
         rng.Select();਍         爀渀最⸀倀愀猀琀攀匀琀爀椀渀最 ⠀∀㰀㼀砀洀ⴀ爀攀瀀氀愀挀攀开琀攀砀琀 笀䄀搀搀 䰀攀愀搀䤀渀 琀攀砀琀 栀攀爀攀紀㼀㸀∀⤀㬀ഀഀ
      } else {਍         䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀 ⠀∀䰀攀愀搀䤀渀 琀愀最 挀愀渀 渀漀琀 戀攀 椀渀猀攀爀琀攀搀 栀攀爀攀⸀∀Ⰰ ∀䌀吀䈀∀⤀㬀ഀഀ
      }਍   紀ഀഀ
}਍椀渀猀攀爀琀䰀攀愀搀䤀渀⠀⤀㬀ഀഀ
]]></MACRO>਍ഀഀ
<MACRO name="Insert Distractor Rationale" hide="true" lang="JScript" id="1501"><![CDATA[਍昀甀渀挀琀椀漀渀 椀渀猀攀爀琀䐀椀猀琀爀愀挀琀漀爀刀愀琀椀漀渀愀氀攀 ⠀⤀ 笀ഀഀ
਍   瘀愀爀 爀渀最 㴀 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀刀愀渀最攀㬀ഀഀ
//   if (rng.IsParentElement("Item")) {਍⼀⼀      眀栀椀氀攀 ⠀爀渀最⸀䌀漀渀琀愀椀渀攀爀一漀搀攀⸀渀漀搀攀一愀洀攀 ℀㴀 ∀䤀琀攀洀∀⤀ 笀ഀഀ
//         rng.SelectElement();਍⼀⼀      紀ഀഀ
//   }਍   椀昀 ⠀爀渀最⸀䘀椀渀搀䤀渀猀攀爀琀䰀漀挀愀琀椀漀渀⠀∀䐀椀猀琀爀愀挀琀漀爀刀愀琀椀漀渀愀氀攀∀⤀⤀ 笀ഀഀ
      rng.SelectContainerContents();਍      爀渀最⸀䌀漀氀氀愀瀀猀攀⠀猀焀䌀漀氀氀愀瀀猀攀䔀渀搀⤀㬀ഀഀ
      rng.InsertElement("DistractorRationale");਍      爀渀最⸀匀攀氀攀挀琀⠀⤀㬀ഀഀ
      rng.InsertElement("P");਍      爀渀最⸀匀攀氀攀挀琀⠀⤀㬀ഀഀ
      rng.PasteString ("<?xm-replace_text {Add Distractor Rationale text here}?>");਍   紀 攀氀猀攀 笀ഀഀ
      rng.MoveToDocumentStart();਍      椀昀 ⠀爀渀最⸀䘀椀渀搀䤀渀猀攀爀琀䰀漀挀愀琀椀漀渀⠀∀䐀椀猀琀爀愀挀琀漀爀刀愀琀椀漀渀愀氀攀∀⤀⤀ 笀ഀഀ
         rng.SelectContainerContents();਍         爀渀最⸀䌀漀氀氀愀瀀猀攀⠀猀焀䌀漀氀氀愀瀀猀攀䔀渀搀⤀㬀ഀഀ
         rng.InsertElement("DistractorRationale");਍         爀渀最⸀匀攀氀攀挀琀⠀⤀㬀ഀഀ
         rng.InsertElement("P");਍         爀渀最⸀匀攀氀攀挀琀⠀⤀㬀ഀഀ
         rng.PasteString ("<?xm-replace_text {Add Distractor Rationale text here}?>");਍      紀 攀氀猀攀 笀ഀഀ
         Application.Alert ("DistractorRationale tag can not be inserted here.", "CTB");਍      紀ഀഀ
   }਍紀ഀഀ
insertDistractorRationale();਍崀崀㸀㰀⼀䴀䄀䌀刀伀㸀ഀഀ
<MACRO name="Insert Assessment" hide="true" lang="JScript" id="1522"><![CDATA[਍昀甀渀挀琀椀漀渀 椀渀猀攀爀琀䄀猀猀攀猀猀洀攀渀琀 ⠀⤀ 笀ഀഀ
   var rng = ActiveDocument.Range;਍   爀渀最⸀䴀漀瘀攀吀漀䐀漀挀甀洀攀渀琀匀琀愀爀琀⠀⤀㬀ഀഀ
   if (rng.FindInsertLocation("Assessment")) {਍      爀渀最⸀䤀渀猀攀爀琀䔀氀攀洀攀渀琀⠀∀䄀猀猀攀猀猀洀攀渀琀∀⤀㬀ഀഀ
      rng.Select();਍   紀 攀氀猀攀 笀ഀഀ
      rng.MoveToDocumentStart();਍      椀昀 ⠀爀渀最⸀䘀椀渀搀䤀渀猀攀爀琀䰀漀挀愀琀椀漀渀⠀∀䄀猀猀攀猀猀洀攀渀琀∀⤀⤀ 笀ഀഀ
         rng.InsertElement("Assessment");਍         爀渀最⸀匀攀氀攀挀琀⠀⤀㬀ഀഀ
      } else {਍         䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀 ⠀∀䄀猀猀攀猀猀洀攀渀琀 琀愀最 挀愀渀 渀漀琀 戀攀 椀渀猀攀爀琀攀搀 栀攀爀攀⸀∀Ⰰ ∀䌀吀䈀∀⤀㬀ഀഀ
      }਍   紀ഀഀ
}਍椀渀猀攀爀琀䄀猀猀攀猀猀洀攀渀琀⠀⤀㬀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀䤀渀猀攀爀琀 匀甀戀吀攀猀琀∀ 栀椀搀攀㴀∀琀爀甀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㄀㔀㈀㈀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
function insertSubTest () {਍   瘀愀爀 爀渀最 㴀 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀刀愀渀最攀㬀ഀഀ
   rng.MoveToDocumentStart();਍   椀昀 ⠀爀渀最⸀䘀椀渀搀䤀渀猀攀爀琀䰀漀挀愀琀椀漀渀⠀∀匀甀戀吀攀猀琀∀⤀⤀ 笀ഀഀ
      rng.InsertElement("SubTest");਍      爀渀最⸀匀攀氀攀挀琀⠀⤀㬀ഀഀ
      rng.InsertElement("ItemSet");਍      爀渀最⸀匀攀氀攀挀琀⠀⤀㬀ഀഀ
      rng.InsertElement("Item");਍      爀渀最⸀匀攀氀攀挀琀⠀⤀㬀ഀഀ
      rng.InsertElement("Stem");਍      爀渀最⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
      rng.InsertElementWithRequired("SelectedResponse");਍      爀渀最⸀匀攀氀攀挀琀⠀⤀㬀ഀഀ
   } else {਍      爀渀最⸀䴀漀瘀攀吀漀䐀漀挀甀洀攀渀琀匀琀愀爀琀⠀⤀㬀ഀഀ
      if (rng.FindInsertLocation("SubTest")) {਍         爀渀最⸀䤀渀猀攀爀琀䔀氀攀洀攀渀琀⠀∀匀甀戀吀攀猀琀∀⤀㬀ഀഀ
         rng.Select();਍         爀渀最⸀䤀渀猀攀爀琀䔀氀攀洀攀渀琀⠀∀䤀琀攀洀匀攀琀∀⤀㬀ഀഀ
         rng.Select();਍         爀渀最⸀䤀渀猀攀爀琀䔀氀攀洀攀渀琀⠀∀䤀琀攀洀∀⤀㬀ഀഀ
         rng.Select();਍         爀渀最⸀䤀渀猀攀爀琀䔀氀攀洀攀渀琀⠀∀匀琀攀洀∀⤀㬀ഀഀ
         rng.SelectAfterContainer();਍         爀渀最⸀䤀渀猀攀爀琀䔀氀攀洀攀渀琀圀椀琀栀刀攀焀甀椀爀攀搀⠀∀匀攀氀攀挀琀攀搀刀攀猀瀀漀渀猀攀∀⤀㬀ഀഀ
         rng.Select();਍      紀 攀氀猀攀 笀ഀഀ
         Application.Alert ("SubTest tag can not be inserted here.", "CTB");਍      紀ഀഀ
   }਍紀ഀഀ
insertSubTest();਍崀崀㸀㰀⼀䴀䄀䌀刀伀㸀ഀഀ
਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀䤀渀猀攀爀琀 䰀椀渀攀猀∀ 栀椀搀攀㴀∀琀爀甀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㄀㄀㔀　∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
function InsertLines () {਍ഀഀ
   var rng = ActiveDocument.Range; ਍   瘀愀爀 爀渀最㄀ 㴀 爀渀最⸀䐀甀瀀氀椀挀愀琀攀㬀ഀഀ
   var rng2 = rng.Duplicate;਍ഀഀ
   if (rng1.IsParentElement("Item")) {਍      眀栀椀氀攀 ⠀爀渀最㄀⸀䌀漀渀琀愀椀渀攀爀一漀搀攀⸀渀漀搀攀一愀洀攀 ℀㴀 ∀䤀琀攀洀∀⤀ 笀ഀഀ
         rng1.SelectElement();਍      紀ഀഀ
      rng1.SelectElement();਍   紀ഀഀ
   if ((rng2.FindInsertLocation("Line")) &&਍       ⠀爀渀最㄀⸀䌀漀渀琀愀椀渀猀 ⠀爀渀最㈀⤀⤀⤀ 笀ഀഀ
      rng2.InsertElement("Line");਍      爀渀最㈀⸀匀攀氀攀挀琀⠀⤀㬀ഀഀ
      rng2.PasteString ("______________________________________________________________________________");਍      爀攀琀甀爀渀 琀爀甀攀㬀ഀഀ
   }਍   爀渀最㈀ 㴀 爀渀最㄀⸀䐀甀瀀氀椀挀愀琀攀㬀ഀഀ
   rng2.Collapse (sqCollapseStart);਍   椀昀 ⠀⠀爀渀最㈀⸀䘀椀渀搀䤀渀猀攀爀琀䰀漀挀愀琀椀漀渀⠀∀䰀椀渀攀∀⤀⤀ ☀☀ഀഀ
       (rng1.Contains (rng2))) {਍      爀渀最㈀⸀䤀渀猀攀爀琀䔀氀攀洀攀渀琀⠀∀䰀椀渀攀∀⤀㬀ഀഀ
      rng2.Select();਍      爀渀最㈀⸀倀愀猀琀攀匀琀爀椀渀最 ⠀∀开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开开∀⤀㬀ഀഀ
      return true;਍   紀ഀഀ
   Application.Alert ("Line could not be inserted because there is no Answer Area.", "CTB");਍   爀攀琀甀爀渀 昀愀氀猀攀㬀ഀഀ
}਍昀甀渀挀琀椀漀渀 䠀漀眀䴀愀渀礀䰀椀渀攀猀吀漀䤀渀猀攀爀琀 ⠀⤀ 笀ഀഀ
   var strPath = Application.Path + "\\config\\ctb\\forms\\InsertLine.xft";਍   琀爀礀 笀ഀഀ
      var objDlg = Application.CreateFormDlg (strPath);਍   紀 挀愀琀挀栀 ⠀攀⤀ 笀ഀഀ
      Application.Alert ("The Preview form can not be located (" + strPath + ")");਍   紀ഀഀ
   objDlg.DoModal();਍   椀昀 ⠀漀戀樀䐀氀最⸀䔀搀椀琀䈀漀砀䄀渀猀眀攀爀⸀吀攀砀琀 㴀㴀 ∀伀䬀∀⤀ 笀ഀഀ
      for (var i=0; i < objDlg.ComboBoxLines.Value; i++) {਍         椀昀 ⠀℀ 䤀渀猀攀爀琀䰀椀渀攀猀⠀⤀⤀ 笀ഀഀ
            break;਍         紀ഀഀ
      }਍   紀ഀഀ
}਍䠀漀眀䴀愀渀礀䰀椀渀攀猀吀漀䤀渀猀攀爀琀 ⠀⤀㬀ഀഀ
]]></MACRO>਍ഀഀ
<MACRO name="Insert TextBox" hide="true" lang="JScript" id="1049"><![CDATA[਍昀甀渀挀琀椀漀渀 䤀渀猀攀爀琀吀攀砀琀䈀漀砀 ⠀䰀椀渀攀䌀漀甀渀琀⤀ 笀ഀഀ
਍   瘀愀爀 爀渀最 㴀 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀刀愀渀最攀㬀 ഀഀ
   var rng1 = rng.Duplicate;਍   瘀愀爀 爀渀最㈀ 㴀 爀渀最⸀䐀甀瀀氀椀挀愀琀攀㬀ഀഀ
਍   椀昀 ⠀爀渀最㄀⸀䤀猀倀愀爀攀渀琀䔀氀攀洀攀渀琀⠀∀䤀琀攀洀∀⤀⤀ 笀ഀഀ
      while (rng1.ContainerNode.nodeName != "Item") {਍         爀渀最㄀⸀匀攀氀攀挀琀䔀氀攀洀攀渀琀⠀⤀㬀ഀഀ
      }਍      爀渀最㄀⸀匀攀氀攀挀琀䔀氀攀洀攀渀琀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀⠀爀渀最㈀⸀䘀椀渀搀䤀渀猀攀爀琀䰀漀挀愀琀椀漀渀⠀∀吀攀砀琀䈀漀砀∀⤀⤀ ☀☀ഀഀ
       (rng1.Contains (rng2))) {਍      爀渀最㈀⸀䤀渀猀攀爀琀䔀氀攀洀攀渀琀⠀∀吀攀砀琀䈀漀砀∀⤀㬀ഀഀ
      rng2.Select();਍      爀渀最㈀⸀䌀漀渀琀愀椀渀攀爀䄀琀琀爀椀戀甀琀攀⠀∀䰀椀渀攀䌀漀甀渀琀∀⤀ 㴀 䰀椀渀攀䌀漀甀渀琀㬀ഀഀ
      rng2.InsertElement("BoxDirection");਍      爀渀最㈀⸀匀攀氀攀挀琀⠀⤀㬀ഀഀ
      rng2.PasteString ("<?xm-replace_text {Add Box Direction text here}?>");਍      爀渀最㈀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
      rng2.InsertElement("BoxAnswer");਍      爀渀最㈀⸀匀攀氀攀挀琀⠀⤀㬀ഀഀ
      rng2.PasteString ("<?xm-replace_text {Add Box Answer text here}?>");਍      爀攀琀甀爀渀 琀爀甀攀㬀ഀഀ
   }਍   爀渀最㈀ 㴀 爀渀最㄀⸀䐀甀瀀氀椀挀愀琀攀㬀ഀഀ
   rng2.Collapse (sqCollapseStart);਍   椀昀 ⠀⠀爀渀最㈀⸀䘀椀渀搀䤀渀猀攀爀琀䰀漀挀愀琀椀漀渀⠀∀吀攀砀琀䈀漀砀∀⤀⤀ ☀☀ഀഀ
       (rng1.Contains (rng2))) {਍      爀渀最㈀⸀䤀渀猀攀爀琀䔀氀攀洀攀渀琀⠀∀吀攀砀琀䈀漀砀∀⤀㬀ഀഀ
      rng2.Select();਍      爀渀最㈀⸀䌀漀渀琀愀椀渀攀爀䄀琀琀爀椀戀甀琀攀⠀∀䰀椀渀攀䌀漀甀渀琀∀⤀ 㴀 䰀椀渀攀䌀漀甀渀琀㬀ഀഀ
      rng2.InsertElement("BoxDirection");਍      爀渀最㈀⸀匀攀氀攀挀琀⠀⤀㬀ഀഀ
      rng2.PasteString ("<?xm-replace_text {Add Box Direction text here}?>");਍      爀渀最㈀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
      rng2.InsertElement("BoxAnswer");਍      爀渀最㈀⸀匀攀氀攀挀琀⠀⤀㬀ഀഀ
      rng2.PasteString ("<?xm-replace_text {Add Box Answer text here}?>");਍      爀攀琀甀爀渀 琀爀甀攀㬀ഀഀ
   }਍   䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀 ⠀∀吀攀砀琀 䈀漀砀 挀漀甀氀搀 渀漀琀 戀攀 椀渀猀攀爀琀攀搀⸀∀Ⰰ ∀䌀吀䈀∀⤀㬀ഀഀ
   return false;਍紀ഀഀ
function HowManyLinesToInsertForTextBox () {਍   瘀愀爀 猀琀爀倀愀琀栀 㴀 䄀瀀瀀氀椀挀愀琀椀漀渀⸀倀愀琀栀 ⬀ ∀尀尀挀漀渀昀椀最尀尀挀琀戀尀尀昀漀爀洀猀尀尀䤀渀猀攀爀琀吀攀砀琀䈀漀砀⸀砀昀琀∀㬀ഀഀ
   try {਍      瘀愀爀 漀戀樀䐀氀最 㴀 䄀瀀瀀氀椀挀愀琀椀漀渀⸀䌀爀攀愀琀攀䘀漀爀洀䐀氀最 ⠀猀琀爀倀愀琀栀⤀㬀ഀഀ
   } catch (e) {਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀 ⠀∀吀栀攀 倀爀攀瘀椀攀眀 昀漀爀洀 挀愀渀 渀漀琀 戀攀 氀漀挀愀琀攀搀 ⠀∀ ⬀ 猀琀爀倀愀琀栀 ⬀ ∀⤀∀⤀㬀ഀഀ
   }਍   漀戀樀䐀氀最⸀䐀漀䴀漀搀愀氀⠀⤀㬀ഀഀ
   if (objDlg.EditBoxAnswer.Text == "OK") {਍      䤀渀猀攀爀琀吀攀砀琀䈀漀砀⠀漀戀樀䐀氀最⸀䌀漀洀戀漀䈀漀砀䰀椀渀攀猀⸀嘀愀氀甀攀⤀㬀ഀഀ
   }਍紀ഀഀ
HowManyLinesToInsertForTextBox ();਍崀崀㸀㰀⼀䴀䄀䌀刀伀㸀ഀഀ
਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀䤀渀猀攀爀琀 䈀漀砀䐀椀爀攀挀琀椀漀渀∀ 栀椀搀攀㴀∀琀爀甀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㄀㄀㌀㐀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
function InsertBoxDirection () {਍ഀഀ
   var rng = ActiveDocument.Range;਍   瘀愀爀 爀渀最㄀ 㴀 爀渀最⸀䐀甀瀀氀椀挀愀琀攀㬀ഀഀ
   var rng2 = rng.Duplicate;਍   椀昀 ⠀爀渀最㄀⸀䤀猀倀愀爀攀渀琀䔀氀攀洀攀渀琀⠀∀䤀琀攀洀∀⤀⤀ 笀ഀഀ
      while (rng1.ContainerNode.nodeName != "Item") {਍         爀渀最㄀⸀匀攀氀攀挀琀䔀氀攀洀攀渀琀⠀⤀㬀ഀഀ
      }਍      爀渀最㄀⸀匀攀氀攀挀琀䔀氀攀洀攀渀琀⠀⤀㬀ഀഀ
   } else {਍      䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀 ⠀∀倀氀攀愀猀攀 瀀氀愀挀攀 礀漀甀爀 挀甀爀猀漀爀 椀渀猀椀搀攀 漀昀 愀渀 䤀琀攀洀⸀∀⤀㬀ഀഀ
      return;਍   紀ഀഀ
   if ((rng2.FindInsertLocation("BoxDirection")) &&਍       ⠀爀渀最㄀⸀䌀漀渀琀愀椀渀猀 ⠀爀渀最㈀⤀⤀⤀ 笀ഀഀ
      rng2.InsertElement("BoxDirection");਍      爀渀最㈀⸀匀攀氀攀挀琀⠀⤀㬀ഀഀ
      rng2.PasteString ("<?xm-replace_text {Add Box Direction text here}?>");਍      爀攀琀甀爀渀㬀ഀഀ
   }਍   爀渀最㈀ 㴀 爀渀最㄀⸀䐀甀瀀氀椀挀愀琀攀㬀ഀഀ
   rng2.Collapse (sqCollapseStart);਍   椀昀 ⠀⠀爀渀最㈀⸀䘀椀渀搀䤀渀猀攀爀琀䰀漀挀愀琀椀漀渀⠀∀䈀漀砀䐀椀爀攀挀琀椀漀渀∀⤀⤀ ☀☀ഀഀ
       (rng1.Contains (rng2))) {਍      爀渀最㈀⸀䤀渀猀攀爀琀䔀氀攀洀攀渀琀⠀∀䈀漀砀䐀椀爀攀挀琀椀漀渀∀⤀㬀ഀഀ
      rng2.Select();਍      爀渀最㈀⸀倀愀猀琀攀匀琀爀椀渀最 ⠀∀㰀㼀砀洀ⴀ爀攀瀀氀愀挀攀开琀攀砀琀 笀䄀搀搀 䈀漀砀 䐀椀爀攀挀琀椀漀渀 琀攀砀琀 栀攀爀攀紀㼀㸀∀⤀㬀ഀഀ
      return;਍   紀ഀഀ
   Application.Alert ("Box Direction could not be inserted either because there is no Text Box or because all Text Boxes already have Box Directions.", "CTB");਍紀ഀഀ
InsertBoxDirection();਍崀崀㸀㰀⼀䴀䄀䌀刀伀㸀ഀഀ
<MACRO name="Insert BoxAnswer" hide="true" lang="JScript" id="1133"><![CDATA[਍昀甀渀挀琀椀漀渀 䤀渀猀攀爀琀䈀漀砀䄀渀猀眀攀爀 ⠀⤀ 笀ഀഀ
਍   瘀愀爀 爀渀最 㴀 䄀挀琀椀瘀攀䐀漀挀甀洀攀渀琀⸀刀愀渀最攀㬀ഀഀ
   var rng1 = rng.Duplicate;਍   瘀愀爀 爀渀最㈀ 㴀 爀渀最⸀䐀甀瀀氀椀挀愀琀攀㬀ഀഀ
   if (rng1.IsParentElement("Item")) {਍      眀栀椀氀攀 ⠀爀渀最㄀⸀䌀漀渀琀愀椀渀攀爀一漀搀攀⸀渀漀搀攀一愀洀攀 ℀㴀 ∀䤀琀攀洀∀⤀ 笀ഀഀ
         rng1.SelectElement();਍      紀ഀഀ
      rng1.SelectElement();਍   紀 攀氀猀攀 笀ഀഀ
      Application.Alert ("Please place your cursor inside of an Item.");਍      爀攀琀甀爀渀㬀ഀഀ
   }਍   椀昀 ⠀⠀爀渀最㈀⸀䘀椀渀搀䤀渀猀攀爀琀䰀漀挀愀琀椀漀渀⠀∀䈀漀砀䐀椀爀攀挀琀椀漀渀∀⤀⤀ ☀☀ഀഀ
       (rng1.Contains (rng2))) {਍      爀渀最㈀⸀䤀渀猀攀爀琀䔀氀攀洀攀渀琀⠀∀䈀漀砀䄀渀猀眀攀爀∀⤀㬀ഀഀ
      rng2.Select();਍      爀渀最㈀⸀倀愀猀琀攀匀琀爀椀渀最 ⠀∀㰀㼀砀洀ⴀ爀攀瀀氀愀挀攀开琀攀砀琀 笀䄀搀搀 䈀漀砀 䄀渀猀眀攀爀 琀攀砀琀 栀攀爀攀紀㼀㸀∀⤀㬀ഀഀ
      return;਍   紀ഀഀ
   rng2 = rng1.Duplicate;਍   爀渀最㈀⸀䌀漀氀氀愀瀀猀攀 ⠀猀焀䌀漀氀氀愀瀀猀攀匀琀愀爀琀⤀㬀ഀഀ
   if ((rng2.FindInsertLocation("BoxAnswer")) &&਍       ⠀爀渀最㄀⸀䌀漀渀琀愀椀渀猀 ⠀爀渀最㈀⤀⤀⤀ 笀ഀഀ
      rng2.InsertElement("BoxAnswer");਍      爀渀最㈀⸀匀攀氀攀挀琀⠀⤀㬀ഀഀ
      rng2.PasteString ("<?xm-replace_text {Add Box Answer text here}?>");਍      爀攀琀甀爀渀㬀ഀഀ
   }਍   䄀瀀瀀氀椀挀愀琀椀漀渀⸀䄀氀攀爀琀 ⠀∀䈀漀砀 䄀渀猀眀攀爀 挀漀甀氀搀 渀漀琀 戀攀 椀渀猀攀爀琀攀搀 攀椀琀栀攀爀 戀攀挀愀甀猀攀 琀栀攀爀攀 椀猀 渀漀 吀攀砀琀 䈀漀砀 漀爀 戀攀挀愀甀猀攀 愀氀氀 吀攀砀琀 䈀漀砀攀猀 愀氀爀攀愀搀礀 栀愀瘀攀 䈀漀砀 䄀渀猀眀攀爀⸀∀Ⰰ ∀䌀吀䈀∀⤀㬀ഀഀ
}਍䤀渀猀攀爀琀䈀漀砀䄀渀猀眀攀爀⠀⤀㬀ഀഀ
]]></MACRO>਍ഀഀ
<MACRO name="Insert Page Break" hide="false" lang="JScript"><![CDATA[਍昀甀渀挀琀椀漀渀 䤀渀猀攀爀琀倀愀最攀䈀爀攀愀欀 ⠀⤀ 笀ഀഀ
   var rng = ActiveDocument.Range;਍   椀昀 ⠀爀渀最⸀䘀椀渀搀䤀渀猀攀爀琀䰀漀挀愀琀椀漀渀 ⠀∀䈀刀∀⤀⤀ 笀ഀഀ
      rng.InsertElement ("BR");਍      爀渀最⸀匀攀氀攀挀琀⠀⤀㬀ഀഀ
      rng.ContainerAttribute ("Type") = "Page";਍   紀ഀഀ
}਍䤀渀猀攀爀琀倀愀最攀䈀爀攀愀欀⠀⤀㬀ഀഀ
]]></MACRO>਍ഀഀ
<MACRO name="Show CTB-General" hide="true" lang="JScript"><![CDATA[਍昀甀渀挀琀椀漀渀 猀栀漀眀䌀吀䈀䜀攀渀攀爀愀氀吀漀漀氀戀愀爀 ⠀⤀ 笀ഀഀ
   var cmdBars = Application.CommandBars;਍   瘀愀爀 挀洀搀䈀愀爀 㴀 挀洀搀䈀愀爀猀⸀椀琀攀洀⠀∀䌀吀䈀 ⴀ 䜀攀渀攀爀愀氀 唀渀椀挀漀搀攀 䌀栀愀爀愀挀琀攀爀猀∀⤀㬀ഀഀ
   cmdBar.Visible = true;਍紀ഀഀ
showCTBGeneralToolbar ();਍崀崀㸀㰀⼀䴀䄀䌀刀伀㸀ഀഀ
<MACRO name="Show CTB-Math" hide="true" lang="JScript"><![CDATA[਍昀甀渀挀琀椀漀渀 猀栀漀眀䌀吀䈀䴀愀琀栀吀漀漀氀戀愀爀 ⠀⤀ 笀ഀഀ
   var cmdBars = Application.CommandBars;਍   瘀愀爀 挀洀搀䈀愀爀 㴀 挀洀搀䈀愀爀猀⸀椀琀攀洀⠀∀䌀吀䈀 ⴀ 䴀愀琀栀 唀渀椀挀漀搀攀 䌀栀愀爀愀挀琀攀爀猀∀⤀㬀ഀഀ
   cmdBar.Visible = true;਍紀ഀഀ
showCTBMathToolbar ();਍崀崀㸀㰀⼀䴀䄀䌀刀伀㸀ഀഀ
਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀䤀渀猀攀爀琀 唀匀瀀愀挀攀∀ 栀椀搀攀㴀∀琀爀甀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㄀㄀　㤀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
function insertUSpace () {਍   匀攀氀攀挀琀椀漀渀⸀䤀渀猀攀爀琀䔀渀琀椀琀礀 ⠀∀唀匀瀀愀挀攀∀⤀㬀ഀഀ
}਍椀渀猀攀爀琀唀匀瀀愀挀攀⠀⤀㬀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䴀搀愀猀栀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㄀　∀ 琀漀漀氀琀椀瀀㴀∀䴀搀愀猀栀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀᐀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开一搀愀猀栀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㄀㄀∀ 琀漀漀氀琀椀瀀㴀∀一搀愀猀栀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀ጀ∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开倀氀甀猀 漀爀 洀椀渀甀猀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀　　∀ 琀漀漀氀琀椀瀀㴀∀倀氀甀猀 漀爀 洀椀渀甀猀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀넀∀⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䄀搀搀椀琀椀漀渀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀　㄀∀ 琀漀漀氀琀椀瀀㴀∀䄀搀搀椀琀椀漀渀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀⬀∀⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䐀椀瘀椀猀椀漀渀 猀礀洀戀漀氀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀　㈀∀ 琀漀漀氀琀椀瀀㴀∀䐀椀瘀椀猀椀漀渀 猀礀洀戀漀氀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀∀⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䴀甀氀琀椀瀀氀椀挀愀琀椀漀渀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀　㌀∀ 琀漀漀氀琀椀瀀㴀∀䴀甀氀琀椀瀀氀椀挀愀琀椀漀渀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀휀∀⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䴀甀氀琀椀瀀氀椀挀愀琀椀漀渀⼀䈀甀氀氀攀爀⼀倀漀椀渀琀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㄀㈀∀ 琀漀漀氀琀椀瀀㴀∀䴀甀氀琀椀瀀氀椀挀愀琀椀漀渀⼀䈀甀氀氀攀爀⼀倀漀椀渀琀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀∀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䔀焀甀愀氀猀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀　㔀∀ 琀漀漀氀琀椀瀀㴀∀䔀焀甀愀氀猀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀㴀∀⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开一漀琀 攀焀甀愀氀 琀漀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀　㘀∀ 琀漀漀氀琀椀瀀㴀∀一漀琀 攀焀甀愀氀 琀漀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀怀∢⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䰀攀猀猀 琀栀愀渀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀　㜀∀ 琀漀漀氀琀椀瀀㴀∀䰀攀猀猀 琀栀愀渀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀㰀∀⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䜀爀攀愀琀攀爀 琀栀愀渀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀　㠀∀ 琀漀漀氀琀椀瀀㴀∀䜀爀攀愀琀攀爀 琀栀愀渀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀㸀∀⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䰀攀猀猀 琀栀愀渀 漀爀 攀焀甀愀氀 琀漀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀　㤀∀ 琀漀漀氀琀椀瀀㴀∀䰀攀猀猀 琀栀愀渀 漀爀 攀焀甀愀氀 琀漀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀搀∢⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䜀爀攀愀琀攀爀 琀栀攀渀 漀爀 攀焀甀愀氀 琀漀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㄀㌀∀ 琀漀漀氀琀椀瀀㴀∀䜀爀攀愀琀攀爀 琀栀攀渀 漀爀 攀焀甀愀氀 琀漀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀攀∢⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䤀渀昀椀渀椀琀礀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㔀　∀ 琀漀漀氀琀椀瀀㴀∀䤀渀昀椀渀椀琀礀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀Ḁ∢⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>  ਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀焀甀愀爀攀 刀漀漀琀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㄀㔀∀ 琀漀漀氀琀椀瀀㴀∀匀焀甀愀爀攀 刀漀漀琀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀ᨀ∢⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开伀瀀攀渀 戀爀愀挀欀攀琀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㄀㘀∀ 琀漀漀氀琀椀瀀㴀∀伀瀀攀渀 戀爀愀挀欀攀琀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀嬀∀⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䌀氀漀猀攀 戀爀愀挀欀攀琀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㄀㜀∀ 琀漀漀氀琀椀瀀㴀∀䌀氀漀猀攀 戀爀愀挀欀攀琀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀崀∀⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开伀瀀攀渀 戀爀愀挀攀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㄀㠀∀ 琀漀漀氀琀椀瀀㴀∀伀瀀攀渀 戀爀愀挀攀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀笀∀⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䌀氀漀猀攀 戀爀愀挀攀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㄀㤀∀ 琀漀漀氀琀椀瀀㴀∀䌀氀漀猀攀 戀爀愀挀攀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀紀∀⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䌀漀瀀礀爀椀最栀琀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㈀　∀ 琀漀漀氀琀椀瀀㴀∀䌀漀瀀礀爀椀最栀琀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀꤀∀⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开刀攀最椀猀琀攀爀攀搀 琀爀愀搀攀洀愀爀欀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㈀㄀∀ 琀漀漀氀琀椀瀀㴀∀刀攀最椀猀琀攀爀攀搀 琀爀愀搀攀洀愀爀欀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀글∀⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开吀爀愀搀攀洀愀爀欀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㈀㈀∀ 琀漀漀氀琀椀瀀㴀∀吀爀愀搀攀洀愀爀欀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀∀∡⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀椀渀最氀攀 漀瀀攀渀 焀甀漀琀攀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㈀㌀∀ 琀漀漀氀琀椀瀀㴀∀匀椀渀最氀攀 漀瀀攀渀 焀甀漀琀攀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀᠀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀椀渀最氀攀 挀氀漀猀攀 焀甀漀琀攀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㈀㐀∀ 琀漀漀氀琀椀瀀㴀∀匀椀渀最氀攀 挀氀漀猀攀 焀甀漀琀攀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀ᤀ∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䄀猀琀攀爀椀猀欀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㈀㔀∀ 琀漀漀氀琀椀瀀㴀∀䄀猀琀攀爀椀猀欀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀⨀∀⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䔀氀氀椀瀀猀攀猀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㈀㠀∀ 琀漀漀氀琀椀瀀㴀∀䔀氀氀椀瀀猀攀猀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀☀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开倀爀椀洀攀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㈀㤀∀ 琀漀漀氀琀椀瀀㴀∀倀爀椀洀攀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀됀∀⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䐀攀最爀攀攀猀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㌀　∀ 琀漀漀氀琀椀瀀㴀∀䐀攀最爀攀攀猀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀�∂⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䄀戀猀漀氀甀琀攀 瘀愀氀甀攀 戀愀爀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㌀㄀∀ 琀漀漀氀琀椀瀀㴀∀䄀戀猀漀氀甀琀攀 瘀愀氀甀攀 戀愀爀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀簀∀⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开倀攀爀瀀攀渀搀椀挀甀氀愀爀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㌀㈀∀ 琀漀漀氀琀椀瀀㴀∀倀攀爀瀀攀渀搀椀挀甀氀愀爀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀ꔀ∢⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开倀愀爀愀氀氀攀氀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㌀㌀∀ 琀漀漀氀琀椀瀀㴀∀倀愀爀愀氀氀攀氀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀─∢⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀椀洀椀氀愀爀 琀漀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㌀㐀∀ 琀漀漀氀琀椀瀀㴀∀匀椀洀椀氀愀爀 琀漀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀縀∀⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䌀漀渀最爀甀攀渀琀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㌀㘀∀ 琀漀漀氀琀椀瀀㴀∀䌀漀渀最爀甀攀渀琀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀䔀∢⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䄀瀀瀀爀漀砀椀洀愀琀攀氀礀 攀焀甀愀氀 琀漀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㌀㜀∀ 琀漀漀氀琀椀瀀㴀∀䄀瀀瀀爀漀砀椀洀愀琀攀氀礀 攀焀甀愀氀 琀漀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀䠀∢⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀攀最洀攀渀琀⼀爀攀瀀攀愀琀椀渀最 搀攀挀椀洀愀氀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㐀㌀∀ 琀漀漀氀琀椀瀀㴀∀匀攀最洀攀渀琀⼀爀攀瀀攀愀琀椀渀最 搀攀挀椀洀愀氀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀꼀∀⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䰀椀渀攀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㌀㤀∀ 琀漀漀氀琀椀瀀㴀∀䰀椀渀攀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀鐀∡⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开刀愀礀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㐀　∀ 琀漀漀氀琀椀瀀㴀∀刀愀礀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀鈀∡⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开吀爀椀愀渀最氀攀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㐀㄀∀ 琀漀漀氀琀椀瀀㴀∀吀爀椀愀渀最氀攀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀؀∢⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开倀䤀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㐀㈀∀ 琀漀漀氀琀椀瀀㴀∀倀䤀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀쀀∃⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开一攀最愀琀椀瘀攀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀　㐀∀ 琀漀漀氀琀椀瀀㴀∀一攀最愀琀椀瘀攀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀준∂⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䔀洀瀀琀礀 猀攀琀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㐀㐀∀ 琀漀漀氀琀椀瀀㴀∀䔀洀瀀琀礀 猀攀琀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀Ԁ∢⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀攀琀 琀栀攀漀爀礀⼀匀甀戀猀攀琀 伀昀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㐀㔀∀ 琀漀漀氀琀椀瀀㴀∀匀攀琀 琀栀攀漀爀礀⼀匀甀戀猀攀琀 伀昀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀舀∢⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀攀琀 琀栀攀漀爀礀⼀匀甀戀猀攀琀 伀昀 漀爀 䔀焀甀愀氀 吀漀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㐀㘀∀ 琀漀漀氀琀椀瀀㴀∀匀攀琀 琀栀攀漀爀礀⼀匀甀戀猀攀琀 伀昀 漀爀 䔀焀甀愀氀 吀漀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀蘀∢⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀攀琀 琀栀攀漀爀礀⼀唀渀椀漀渀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㐀㜀∀ 琀漀漀氀琀椀瀀㴀∀匀攀琀 琀栀攀漀爀礀⼀唀渀椀漀渀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀⨀∢⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀攀琀 琀栀攀漀爀礀⼀䤀渀琀攀爀猀攀挀琀椀漀渀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㐀㠀∀ 琀漀漀氀琀椀瀀㴀∀匀攀琀 琀栀攀漀爀礀⼀䤀渀琀攀爀猀攀挀琀椀漀渀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀⤀∢⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开吀栀攀爀攀昀漀爀攀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㈀㈀　∀ 琀漀漀氀琀椀瀀㴀∀吀栀攀爀攀昀漀爀攀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀㐀∢⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开一琀栀 爀漀漀琀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㔀㄀∀ 琀漀漀氀琀椀瀀㴀∀一琀栀 爀漀漀琀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀ᬀ∢⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䜀爀攀攀欀 氀攀琀琀攀爀⼀匀洀愀氀氀 䰀攀琀琀攀爀 吀栀攀琀愀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㔀㈀∀ 琀漀漀氀琀椀瀀㴀∀䜀爀攀攀欀 氀攀琀琀攀爀⼀匀洀愀氀氀 䰀攀琀琀攀爀 吀栀攀琀愀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀렀∃⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䜀爀攀攀欀 氀攀琀琀攀爀⼀䌀愀瀀椀琀愀氀 䰀攀琀琀攀爀 䌀栀椀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㔀㌀∀ 琀漀漀氀琀椀瀀㴀∀䜀爀攀攀欀 氀攀琀琀攀爀⼀䌀愀瀀椀琀愀氀 䰀攀琀琀攀爀 䌀栀椀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀꜀∃⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䜀爀攀攀欀 氀攀琀琀攀爀⼀一ⴀ䄀爀礀 匀甀洀洀愀琀椀漀渀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㔀㐀∀ 琀漀漀氀琀椀瀀㴀∀䜀爀攀攀欀 氀攀琀琀攀爀⼀一ⴀ䄀爀礀 匀甀洀洀愀琀椀漀渀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀ᄀ∢⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䜀爀攀攀欀 氀攀琀琀攀爀⼀匀洀愀氀氀 䰀攀琀琀攀爀 䄀氀瀀栀愀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㔀㔀∀ 琀漀漀氀琀椀瀀㴀∀䜀爀攀攀欀 氀攀琀琀攀爀⼀匀洀愀氀氀 䰀攀琀琀攀爀 䄀氀瀀栀愀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀넀∃⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀瀀攀爀猀挀爀椀瀀琀 　∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㔀㘀∀ 琀漀漀氀琀椀瀀㴀∀匀甀瀀攀爀猀挀爀椀瀀琀 　∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀瀀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀瀀攀爀猀挀爀椀瀀琀 ㄀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㔀㜀∀ 琀漀漀氀琀椀瀀㴀∀匀甀瀀攀爀猀挀爀椀瀀琀 ㄀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀뤀∀⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀瀀攀爀猀挀爀椀瀀琀 ㈀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㔀㠀∀ 琀漀漀氀琀椀瀀㴀∀匀甀瀀攀爀猀挀爀椀瀀琀 ㈀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀눀∀⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀瀀攀爀猀挀爀椀瀀琀 ㌀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㔀㤀∀ 琀漀漀氀琀椀瀀㴀∀匀甀瀀攀爀猀挀爀椀瀀琀 ㌀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀대∀⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀瀀攀爀猀挀爀椀瀀琀 㐀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㘀　∀ 琀漀漀氀琀椀瀀㴀∀匀甀瀀攀爀猀挀爀椀瀀琀 㐀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀琀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀瀀攀爀猀挀爀椀瀀琀 㔀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㘀㄀∀ 琀漀漀氀琀椀瀀㴀∀匀甀瀀攀爀猀挀爀椀瀀琀 㔀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀甀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀瀀攀爀猀挀爀椀瀀琀 㘀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㘀㈀∀ 琀漀漀氀琀椀瀀㴀∀匀甀瀀攀爀猀挀爀椀瀀琀 㘀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀瘀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀瀀攀爀猀挀爀椀瀀琀 㜀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㘀㌀∀ 琀漀漀氀琀椀瀀㴀∀匀甀瀀攀爀猀挀爀椀瀀琀 㜀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀眀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀瀀攀爀猀挀爀椀瀀琀 㠀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㘀㐀∀ 琀漀漀氀琀椀瀀㴀∀匀甀瀀攀爀猀挀爀椀瀀琀 㠀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀砀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀瀀攀爀猀挀爀椀瀀琀 㤀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㘀㔀∀ 琀漀漀氀琀椀瀀㴀∀匀甀瀀攀爀猀挀爀椀瀀琀 㤀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀礀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀瀀攀爀猀挀爀椀瀀琀 ⬀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㘀㘀∀ 琀漀漀氀琀椀瀀㴀∀匀甀瀀攀爀猀挀爀椀瀀琀 ⬀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀稀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀瀀攀爀猀挀爀椀瀀琀 ⴀ∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㘀㜀∀ 琀漀漀氀琀椀瀀㴀∀匀甀瀀攀爀猀挀爀椀瀀琀 ⴀ∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀笀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀瀀攀爀猀挀爀椀瀀琀 㴀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㘀㠀∀ 琀漀漀氀琀椀瀀㴀∀匀甀瀀攀爀猀挀爀椀瀀琀 㴀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀簀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀瀀攀爀猀挀爀椀瀀琀 ⠀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㘀㤀∀ 琀漀漀氀琀椀瀀㴀∀匀甀瀀攀爀猀挀爀椀瀀琀 ⠀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀紀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀瀀攀爀猀挀爀椀瀀琀 ⤀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㜀　∀ 琀漀漀氀琀椀瀀㴀∀匀甀瀀攀爀猀挀爀椀瀀琀 ⤀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀縀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀瀀攀爀猀挀爀椀瀀琀 渀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㜀㄀∀ 琀漀漀氀琀椀瀀㴀∀匀甀瀀攀爀猀挀爀椀瀀琀 渀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀缀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀戀猀挀爀椀瀀琀 　∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㜀㈀∀ 琀漀漀氀琀椀瀀㴀∀匀甀戀猀挀爀椀瀀琀 　∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀耀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀戀猀挀爀椀瀀琀 ㄀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㜀㌀∀ 琀漀漀氀琀椀瀀㴀∀匀甀戀猀挀爀椀瀀琀 ㄀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀脀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀戀猀挀爀椀瀀琀 ㈀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㜀㐀∀ 琀漀漀氀琀椀瀀㴀∀匀甀戀猀挀爀椀瀀琀 ㈀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀舀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀戀猀挀爀椀瀀琀 ㌀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㜀㔀∀ 琀漀漀氀琀椀瀀㴀∀匀甀戀猀挀爀椀瀀琀 ㌀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀茀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀戀猀挀爀椀瀀琀 㐀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㜀㘀∀ 琀漀漀氀琀椀瀀㴀∀匀甀戀猀挀爀椀瀀琀 㐀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀萀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀戀猀挀爀椀瀀琀 㔀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㜀㜀∀ 琀漀漀氀琀椀瀀㴀∀匀甀戀猀挀爀椀瀀琀 㔀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀蔀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀戀猀挀爀椀瀀琀 㘀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㜀㠀∀ 琀漀漀氀琀椀瀀㴀∀匀甀戀猀挀爀椀瀀琀 㘀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀蘀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀戀猀挀爀椀瀀琀 㜀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㄀㜀㤀∀ 琀漀漀氀琀椀瀀㴀∀匀甀戀猀挀爀椀瀀琀 㜀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀蜀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀戀猀挀爀椀瀀琀 㠀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㈀　　∀ 琀漀漀氀琀椀瀀㴀∀匀甀戀猀挀爀椀瀀琀 㠀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀蠀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀戀猀挀爀椀瀀琀 㤀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㈀　㄀∀ 琀漀漀氀琀椀瀀㴀∀匀甀戀猀挀爀椀瀀琀 㤀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀褀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀戀猀挀爀椀瀀琀 ⬀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㈀　㈀∀ 琀漀漀氀琀椀瀀㴀∀匀甀戀猀挀爀椀瀀琀 ⬀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀言∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀戀猀挀爀椀瀀琀 ⴀ∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㈀　㌀∀ 琀漀漀氀琀椀瀀㴀∀匀甀戀猀挀爀椀瀀琀 ⴀ∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀謀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀戀猀挀爀椀瀀琀 㴀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㈀　㐀∀ 琀漀漀氀琀椀瀀㴀∀匀甀戀猀挀爀椀瀀琀 㴀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀谀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀戀猀挀爀椀瀀琀 ⠀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㈀　㔀∀ 琀漀漀氀琀椀瀀㴀∀匀甀戀猀挀爀椀瀀琀 ⠀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀贀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开匀甀戀猀挀爀椀瀀琀 ⤀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㈀　㘀∀ 琀漀漀氀琀椀瀀㴀∀匀甀戀猀挀爀椀瀀琀 ⤀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀踀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䄀挀挀攀渀琀攀搀 愀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㈀　㜀∀ 琀漀漀氀琀椀瀀㴀∀䄀挀挀攀渀琀攀搀 愀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀∀⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䄀挀挀攀渀琀攀搀 攀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㈀　㠀∀ 琀漀漀氀琀椀瀀㴀∀䄀挀挀攀渀琀攀搀 攀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀∀⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䄀挀挀攀渀琀攀搀 椀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㈀　㤀∀ 琀漀漀氀琀椀瀀㴀∀䄀挀挀攀渀琀攀搀 椀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀∀⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䄀挀挀攀渀琀攀搀 漀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㈀㄀　∀ 琀漀漀氀琀椀瀀㴀∀䄀挀挀攀渀琀攀搀 漀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀∀⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䄀挀挀攀渀琀攀搀 甀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㈀㄀㄀∀ 琀漀漀氀琀椀瀀㴀∀䄀挀挀攀渀琀攀搀 甀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀切∀⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䰀椀最愀琀甀爀攀 昀椀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㈀㄀㈀∀ 琀漀漀氀琀椀瀀㴀∀䰀椀最愀琀甀爀攀 昀椀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀Ā⋻⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开吀栀椀渀 匀瀀愀挀攀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㈀㄀㜀∀ 琀漀漀氀琀椀瀀㴀∀吀栀椀渀 匀瀀愀挀攀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀ऀ∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开攀洀 匀瀀愀挀攀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㈀㄀㠀∀ 琀漀漀氀琀椀瀀㴀∀攀洀 匀瀀愀挀攀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀̀∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开攀渀 匀瀀愀挀攀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㈀㄀㤀∀ 琀漀漀氀琀椀瀀㴀∀攀渀 匀瀀愀挀攀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀Ȁ∠⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开昀甀渀挀琀椀漀渀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㈀㄀㘀∀ 琀漀漀氀琀椀瀀㴀∀昀甀渀挀琀椀漀渀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀鈀∁⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䌀椀爀挀氀攀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㈀㄀㐀∀ 琀漀漀氀琀椀瀀㴀∀䌀椀爀挀氀攀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀餀∢⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䌀攀渀琀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㈀㄀㔀∀ 琀漀漀氀琀椀瀀㴀∀䌀攀渀琀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀ꈀ∀⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀䴀䄀䌀刀伀 渀愀洀攀㴀∀开䄀渀最氀攀∀ 栀椀搀攀㴀∀昀愀氀猀攀∀ 氀愀渀最㴀∀䨀匀挀爀椀瀀琀∀ 椀搀㴀∀㈀㈀㈀　∀ 琀漀漀氀琀椀瀀㴀∀䄀渀最氀攀∀㸀㰀℀嬀䌀䐀䄀吀䄀嬀ഀഀ
   if (Selection.ContainerNode.nodeName == "FONT") {਍      匀攀氀攀挀琀椀漀渀⸀匀攀氀攀挀琀䄀昀琀攀爀䌀漀渀琀愀椀渀攀爀⠀⤀㬀ഀഀ
   }਍   椀昀 ⠀匀攀氀攀挀琀椀漀渀⸀䌀愀渀䤀渀猀攀爀琀 ⠀∀䘀伀一吀∀⤀⤀ 笀ഀഀ
      Selection.InsertElement ("FONT");਍      匀攀氀攀挀琀椀漀渀⸀倀愀猀琀攀匀琀爀椀渀最䄀猀吀攀砀琀 ⠀∀ ∢⤀㬀ഀഀ
      Selection.ContainerAttribute ("FACE") = "CTB Math Serif";਍   紀ഀഀ
]]></MACRO>਍㰀⼀䴀䄀䌀刀伀匀㸀ഀഀ
