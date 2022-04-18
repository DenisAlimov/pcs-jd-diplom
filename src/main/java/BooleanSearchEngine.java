import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {

    HashMap<String, List<PageEntry>> wordList = new HashMap<>(); // String - ключ слово, по которому хранится список с ответами на запрос.

    public BooleanSearchEngine(File pdfsDir) throws IOException {

        for (File pdf : Objects.requireNonNull(pdfsDir.listFiles())) { //Для каждого PDF файла в папке
            if (pdf.isDirectory())
                continue;
            var doc = new PdfDocument(new PdfReader(pdf)); //Создаем java pdfDoc

            for (int pageNum = 1; pageNum <= doc.getNumberOfPages(); pageNum++) { // В котором в цикле пробегаемся по всем страницам TODO: Поиск надо осуществлять с 1-ой страницы.
                PdfPage page = doc.getPage(pageNum); // Получаем объект страницы

                var text = PdfTextExtractor.getTextFromPage(page); //Извлекаем текст со страницы

                var words = text.split("\\P{IsAlphabetic}+"); //Текст разбиваем на массив слов

                Map<String, Integer> freqs = new HashMap<>();
                for (var word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    freqs.put(word.toLowerCase(), freqs.getOrDefault(word, 0) + 1); // Подсчитываю частоту использования слов
                }

                for (Map.Entry<String, Integer> entry : freqs.entrySet()) {
                    List<PageEntry> pagesList = new ArrayList<>();
                    if (wordList.containsKey(entry.getKey())) {
                        pagesList = wordList.get(entry.getKey());
                    }
                    pagesList.add(new PageEntry(pdf.getName(), pageNum, entry.getValue()));
                    wordList.put(entry.getKey(), pagesList);
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        if (wordList.containsKey(word)) {
            Collections.sort(wordList.get(word));
            return wordList.get(word);
        }
        return Collections.emptyList();
    }
}