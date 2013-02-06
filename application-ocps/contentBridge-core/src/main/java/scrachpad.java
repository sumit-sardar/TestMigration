import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class scrachpad {
	
	public static void main(String[] args) {
		String decXML="	<element_package allow_revisit=\"\" allow_revisit_on_restart=\"\" eid=\"38396302\" iid=\"9D_RE_Sample_A_copy\" xslt_ver=\"145\">.<item_canvas id=\"IC\"><panel height=\"24\" id=\"sd1\" layout=\"vertical\" left_padding=\"12\" spacing=\"12\" stereotype=\"directions\" width=\"790\" x=\"0\" y=\"79\"><text_widget halign=\"left\" id=\"widget5019721000\" width=\"761\"><![CDATA[<b>Read the passage. Then answer the question below the passage.</b>]]></text_widget></panel><panel height=\"56\" id=\"stem1\" layout=\"vertical\" left_padding=\"12\" spacing=\"12\" stereotype=\"stem\" width=\"500\" x=\"0\" y=\"103\"><text_widget halign=\"left\" id=\"widget5019721001\" width=\"471\"><![CDATA[Ted looked for a book. After he found it, he went to the front desk to check it out.]]></text_widget><text_widget alt_text=\"Ted is most likely at, a\" halign=\"left\" id=\"widget5019721002\" width=\"471\"><![CDATA[Ted is most likely at a]]></text_widget></panel><answer_area_panel height=\"156\" id=\"A1\" layout=\"vertical\" left_padding=\"12\" spacing=\"12\" stereotype=\"answerArea\" top_padding=\"12\" width=\"488\" x=\"12\" y=\"166\"><answer_choice_widget layout=\"horizontal\" spacing=\"6\" width=\"476\"><selector_widget identifier=\"A\" valign=\"top\" /><text_widget halign=\"left\" id=\"widget5019721005\" width=\"429\"><![CDATA[bank]]></text_widget><rationale>You should have chosen \"C,\" the one that goes with the word \"library.\"</rationale></answer_choice_widget><answer_choice_widget layout=\"horizontal\" spacing=\"6\" width=\"476\"><selector_widget identifier=\"B\" valign=\"top\" /><text_widget halign=\"left\" id=\"widget5019721009\" width=\"429\"><![CDATA[store]]></text_widget><rationale>You should have chosen \"C,\" the one that goes with the word \"library.\"</rationale></answer_choice_widget><answer_choice_widget layout=\"horizontal\" spacing=\"6\" width=\"476\"><selector_widget identifier=\"C\" valign=\"top\" /><text_widget halign=\"left\" id=\"widget5019721013\" width=\"429\"><![CDATA[library]]></text_widget><rationale>Correct!</rationale></answer_choice_widget><answer_choice_widget layout=\"horizontal\" spacing=\"6\" width=\"476\"><selector_widget identifier=\"D\" valign=\"top\" /><text_widget halign=\"left\" id=\"widget5019721017\" width=\"429\"><![CDATA[restaurant]]></text_widget><rationale>You should have chosen \"C,\" the one that goes with the word \"library.\"</rationale></answer_choice_widget></answer_area_panel></item_canvas><item_model answer_text=\"\" answered=\"0\" correct=\"C\" eid=\"38396302\" iid=\"9D_RE_Sample_A_copy\" marked=\"0\" number=\"0\" score=\"0\" type=\"question\"><toolbar FCAT_formula_card=\"not provisioned\" TN_formula_card=\"not provisioned\" cm_ruler=\"not provisioned\" eraser=\"required\" half_inch_ruler=\"not provisioned\" highlighter=\"required\" mm_ruler=\"not provisioned\" oneeighth_inch_ruler=\"not provisioned\" option_eliminator=\"required\" protractor=\"not provisioned\" scientific_calculator=\"not provisioned\" scratchpad=\"required\" standard_calculator=\"not provisioned\" straight_edge=\"not provisioned\" /><interaction max_choices=\"1\" type=\"choice\"><selector identifier=\"A\" obj_id_ref=\"A\" user_action=\"n\" /><selector identifier=\"B\" obj_id_ref=\"B\" user_action=\"n\" /><selector identifier=\"C\" obj_id_ref=\"C\" user_action=\"n\" /><selector identifier=\"D\" obj_id_ref=\"D\" user_action=\"n\" /></interaction></item_model></element_package>";
		System.out.println(decXML.trim());
		
		Pattern pattern=Pattern.compile("id=\"widget[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]");
		Matcher matcher =pattern.matcher(decXML);
		while(matcher.find())
		{
			 for (int i = 0; i<=matcher.groupCount(); i++)
		        {
		            System.out.println(matcher.group(i));
		        }
		}
	   String newString=matcher.replaceAll("id=\"");
	   System.out.println(newString.trim());
		
	}

}
