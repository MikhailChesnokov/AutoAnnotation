import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

class Main {
    private final static int compressionPercent = 30;
    private final static IntUnaryOperator skipSentencesCount  = sentencesNumber -> sentencesNumber * (100 - compressionPercent) / 2 / 100;
    private final static IntUnaryOperator limitSentencesCount = sentencesNumber -> sentencesNumber * compressionPercent / 100;
    private final static BiFunction<Integer,String,Integer> frequencyAccumulatorFunc = (acc, token) -> acc + TokenProcessor.frequencies.get(token);


    public static void main(String[] args) {
        List<String> myStemOutput = MyStemRunner.run();

        Map<String,List<String>> sentenceToTokenList = myStemOutput
                .stream()
                .flatMap(SentenceProcessor::splitParagraphIntoSentences)
                .collect(Collectors.toMap(SentenceProcessor::removeAllTokens, sentence -> SentenceProcessor
                        .fetchTokenList(sentence)
                        .stream()
                        .filter(TokenProcessor::isNotStopWord)
                        .collect(Collectors.toList())));

        sentenceToTokenList.forEach((sentence, tokens) -> tokens.forEach(TokenProcessor::incrementTokenFrequency));

        Map<String,Integer> sentenceToWeight = sentenceToTokenList
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, map -> map.getValue()
                        .stream()
                        .reduce(0, frequencyAccumulatorFunc, Integer::sum)));

        final Map<String, Integer> endSentenceToWeight = sentenceToWeight
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .skip(skipSentencesCount.applyAsInt(sentenceToWeight.size()))
                .limit(limitSentencesCount.applyAsInt(sentenceToWeight.size()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        myStemOutput
                .stream()
                .flatMap(SentenceProcessor::splitParagraphIntoSentences)
                .map(SentenceProcessor::removeAllTokens)
                .filter(endSentenceToWeight::containsKey)
                .forEach(System.out::println);
    }
}