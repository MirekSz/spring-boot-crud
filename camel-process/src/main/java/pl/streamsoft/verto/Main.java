
package pl.streamsoft.verto;

import org.eclipse.webtools.catalogue.BookType;
import org.eclipse.webtools.catalogue.Get;

public class Main {

	public static void main(final String[] args) {
		Good good = new Good();
		Get parameters = new Get();
		parameters.setIndex("asd");
		BookType book = good.getGoodWebServicePort().getBook(parameters);
		System.out.println(book.getTitle());
		System.out.println(book.getAuthor());
		System.out.println(book.getDate());

	}

}
