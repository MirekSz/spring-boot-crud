package hello.auction;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addFormatters(FormatterRegistry formatterRegistry) {
		formatterRegistry.addConverter(new Converter<LocalDate, String>() {

			@Override
			public String convert(LocalDate arg0) {
				Locale locale = LocaleContextHolder.getLocale();
				return DateTimeFormatter.ofPattern("dd.MM.yyyy").format(arg0);
			}

		});

		formatterRegistry.addConverter(new Converter<String, LocalDate>() {
			@Override
			public LocalDate convert(String arg0) {
				Locale locale = LocaleContextHolder.getLocale();
				return LocalDate.parse(arg0, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
			}

		});
	}
}
