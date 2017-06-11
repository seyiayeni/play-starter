package formatters;

import play.data.format.Formatters;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

public class NumberFormatter extends Formatters.AnnotationFormatter<NumberFormat, Double>{

	@Override
	public Double parse(NumberFormat arg0, String text, Locale arg2) throws ParseException {
		return Double.parseDouble(text.replaceAll(",", ""));
	}

	@Override
	public String print(NumberFormat arg0, Double object, Locale arg2) {
		return format(object);
	}

	public static String format(Double object) {
		DecimalFormat df = new DecimalFormat("#,###");
		return df.format(object);
	}

}
