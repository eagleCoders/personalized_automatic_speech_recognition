package org.pasr.asr.dictionary;

import org.pasr.asr.Configuration;
import org.pasr.prep.corpus.Word;
import org.pasr.prep.corpus.WordSequence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.getLevenshteinDistance;


public class Dictionary extends LinkedHashMap<String, String> {
    public Dictionary(){
        unknownWords_ = new ArrayList<>();
    }

    public static Dictionary createFromStream (InputStream inputStream) {
        Dictionary dictionary = new Dictionary();

        Scanner scanner = new Scanner(inputStream);
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();

            int indexOfSeparation = line.indexOf(" ");

            dictionary.put(line.substring(0, indexOfSeparation),
                    line.substring(indexOfSeparation + 1));
        }
        scanner.close();

        return dictionary;
    }

    private List<String> getPhones(String string){
        String phones = get(string);

        if(phones == null){
            return autoPronounce(string);
        }
        else {
            return Arrays.asList(phones.trim().split(" "));
        }
    }

    private List<String> getPhones(Word word){
        return getPhones(word.toString());
    }

    public List<List<String>> getPhones(WordSequence wordSequence){
        return wordSequence.stream()
            .map(this :: getPhones)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<String> getPhonesInLine(WordSequence wordSequence){
        List<String> list = new ArrayList<>();

        getPhones(wordSequence).stream()
            .forEach(list :: addAll);

        return list;
    }

    public Map<String, String> getEntriesByKey(String key){
        return entrySet().stream().
            filter(entry -> entry.getKey().equals(key) ||
                entry.getKey().matches(key + "\\([0-9]+\\)")).
            collect(Collectors.toMap(Map.Entry:: getKey, Map.Entry:: getValue));
    }

    public List<String> getUnknownWords(){
        return unknownWords_;
    }

    private Set<String> getUniqueWords(){
        return keySet().stream().
            filter(entry -> !entry.contains("(")).
            collect(Collectors.toSet());
    }

    /**
     * @param count
     *     The number of words to return
     */
    private List<String> fuzzyMatch(String string, int count){
        String[] bestMatches = new String[count];
        double[] bestDistances = new double[count];
        for(int i = 0;i < count;i++){
            bestDistances[i] = Double.POSITIVE_INFINITY;
        }

        for(String word : getUniqueWords()){
            double distance = getLevenshteinDistance(string, word);

            for(int i = 0;i < count;i++){
                if(distance < bestDistances[i]){
                    bestDistances[i] = distance;
                    bestMatches[i] = word;
                    break;
                }
            }
        }

        return Arrays.asList(bestMatches);
    }

    public List<String> fuzzyMatch(String string){
        return fuzzyMatch(string, 5);
    }

    @Override
    public String put (String key, String value){
        if(!containsKey(key)) {
            super.put(key, value);
            return null;
        }

        int index = 1;
        String currentKey = key + "(" + index + ")";
        while(containsKey(currentKey)){
            // if the given value already exists inside the dictionary, don't put it again
            if(get(currentKey).equals(value)){
                return null;
            }

            index++;
            currentKey = key + "(" + index + ")";
        }

        super.put(currentKey, value);

        return null;
    }

    public void addUnknownWord(String word){
        if(!unknownWords_.contains(word)) {
            unknownWords_.add(word);
        }
    }

    public void remove(String key){
        if(super.remove(key) == null){
            return;
        }

        int index = 2;
        while(super.remove(key + "(" + index + ")") != null){
            index++;
        }
    }

    public void removeUnknownWord(String word){
        unknownWords_.remove(word);
    }

    public static Dictionary getDefaultDictionary() throws FileNotFoundException {
        return Dictionary.createFromStream(new FileInputStream(
            Configuration.getDefaultConfiguration().getDictionaryPath()
        ));
    }

    public void exportToStream(OutputStream outputStream) {
        // Sort the entries of the dictionary based on the key length. This will ensure that
        // "the(1)" is below "the" when the dictionary is saved to the file.
        List<Map.Entry<String, String>> entries = new ArrayList<>(entrySet());

        Collections.sort(entries, (e1, e2) -> e1.getKey().length() - e2.getKey().length());

        PrintWriter printWriter = new PrintWriter(outputStream);
        for (Map.Entry<String, String> entry : entries) {
            printWriter.write(entry.getKey() + " " + entry.getValue() + "\n");
        }
        printWriter.close();
    }

    public static List<String> autoPronounce(String string){
        List<String> list = new ArrayList<>();

        for(char ch : string.trim().toUpperCase().toCharArray()){
            list.add(String.valueOf(ch));
        }

        return list;
    }

    private final List<String> unknownWords_;

}
