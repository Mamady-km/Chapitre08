package bookstoread;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class BookShelfSpec {

    private BookShelf shelf;

    private Book effectiveJava;
    private Book codeComplete;
    private Book mythical;
    private Book cleanCode;

    @BeforeEach
    void init() {

        shelf = new BookShelf();

        effectiveJava = new Book(
                "Effective Java",
                "Joshua Bloch",
                LocalDate.of(2008, Month.MAY, 8));

        codeComplete = new Book(
                "Code Complete",
                "Steve McConnell",
                LocalDate.of(2004, Month.JUNE, 9));

        mythical = new Book(
                "The Mythical Man-Month",
                "Frederick Brooks",
                LocalDate.of(1975, Month.JANUARY, 1));

        cleanCode = new Book(
                "Clean Code",
                "Robert C. Martin",
                LocalDate.of(2008, Month.AUGUST, 1));
    }

    @Test
    void shelfEmptyWhenNoBookAdded() {
        assertTrue(shelf.books().isEmpty());
    }

    @Test
    void bookshelfContainsTwoBooksWhenTwoBooksAdded() {

        shelf.add(effectiveJava, codeComplete);

        assertEquals(2, shelf.books().size());
    }

    @Test
    void emptyBookShelfWhenAddIsCalledWithoutBooks() {

        shelf.add();

        assertTrue(shelf.books().isEmpty());
    }

    @Test
    void booksReturnedFromBookShelfIsImmutableForClient() {

        shelf.add(effectiveJava, codeComplete);

        List<Book> books = shelf.books();

        assertThrows(
                UnsupportedOperationException.class,
                () -> books.add(mythical)
        );
    }

    @Test
    void bookshelfArrangedByBookTitle() {

        shelf.add(effectiveJava, codeComplete, mythical);

        List<Book> books = shelf.arrange();

        assertEquals(
                List.of(codeComplete, effectiveJava, mythical),
                books
        );
    }

    @Test
    void booksInBookShelfAreInInsertionOrderAfterCallingArrange() {

        shelf.add(effectiveJava, codeComplete, mythical);

        shelf.arrange();

        assertEquals(
                List.of(effectiveJava, codeComplete, mythical),
                shelf.books()
        );
    }

    @Test
    void bookshelfArrangedByUserProvidedCriteria() {

        shelf.add(effectiveJava, codeComplete, mythical);

        List<Book> books =
                shelf.arrange(
                        Comparator.comparing(Book::getTitle)
                                .reversed()
                );

        assertEquals(
                List.of(mythical, effectiveJava, codeComplete),
                books
        );
    }

    @Test
    @DisplayName("books inside bookshelf are grouped by publication year")
    void groupBooksInsideBookShelfByPublicationYear() {

        shelf.add(
                effectiveJava,
                codeComplete,
                mythical,
                cleanCode
        );

        Map<Year, List<Book>> booksByPublicationYear =
                shelf.groupByPublicationYear();

        assertThat(booksByPublicationYear)
                .containsKey(Year.of(2008))
                .containsKey(Year.of(2004))
                .containsKey(Year.of(1975));

        assertThat(booksByPublicationYear.get(Year.of(2008)))
                .containsExactlyInAnyOrder(
                        effectiveJava,
                        cleanCode
                );

        assertThat(booksByPublicationYear.get(Year.of(2004)))
                .containsExactly(codeComplete);

        assertThat(booksByPublicationYear.get(Year.of(1975)))
                .containsExactly(mythical);
    }

    @Test
    @DisplayName("books inside bookshelf are grouped by author")
    void groupBooksInsideBookShelfByAuthor() {

        shelf.add(
                effectiveJava,
                codeComplete,
                mythical,
                cleanCode
        );

        Map<String, List<Book>> booksByAuthor =
                shelf.groupBy(Book::getAuthor);

        assertThat(booksByAuthor)
                .containsKey("Joshua Bloch")
                .containsKey("Steve McConnell")
                .containsKey("Frederick Brooks")
                .containsKey("Robert C. Martin");
    }
}