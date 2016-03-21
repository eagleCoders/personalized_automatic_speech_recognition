package org.postp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.utilities.Utilities.collectionToArray;


public class Corpus {
    public Corpus(String textCorpusPath) throws FileNotFoundException {
        this(textCorpusPath, false);
    }

    public Corpus(String textCorpusPath, boolean removePunctuationMarks) throws FileNotFoundException {
        ArrayList<TextLine> lines = new ArrayList<TextLine>();

        Scanner scanner = new Scanner(new File(textCorpusPath));
        while (scanner.hasNextLine()) {
            lines.add(new TextLine(scanner.nextLine(), removePunctuationMarks));
        }
        scanner.close();

        lines_ = new TextLine[lines.size()];
        collectionToArray(lines, lines_);
    }

    public TextLine[] getLines() {
        return lines_;
    }

    public void setWordSeparator(String wordSeparator) {
        for (TextLine textLine : lines_) {
            textLine.setWordSeparator(wordSeparator);
        }
    }

    public String toSingleLine() {
        StringBuilder stringBuilder = new StringBuilder();
        for (TextLine textLine : lines_) {
            stringBuilder.append(textLine.getLine());
        }

        return stringBuilder.toString();
    }

    private final TextLine[] lines_;

}
