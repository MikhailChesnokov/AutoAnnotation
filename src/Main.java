import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

class Main {
    private final static int compressionPercent = 30;
    private final static IntUnaryOperator skipSentencesCount  = sentencesNumber -> sentencesNumber * (100 - compressionPercent) / 2 / 100;
    private final static IntUnaryOperator limitSentencesCount = sentencesNumber -> sentencesNumber * compressionPercent / 100;
    private final static BiFunction<Integer,String,Integer> frequencyAccumulatorFunc = (acc, token) -> acc + TokenProcessor.frequencies.get(token);


    public static void main(String[] args) {
        Map<String,List<String>> sentenceToTokenList = MyStemRunner.run()
                .stream()
                .flatMap(SentenceProcessor::splitParagraphIntoSentences)
                .collect(Collectors.toMap(SentenceProcessor::removeAllTokens, sentence -> SentenceProcessor
                        .fetchTokenList(sentence)
                        .stream()
                        .filter(TokenProcessor::isNotStopWord)
                        .collect(Collectors.toList())));

        sentenceToTokenList.forEach((s, tokens) -> tokens.forEach(TokenProcessor::incrementTokenFrequency));

        Map<String,Integer> sentenceToWeight = sentenceToTokenList.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, map -> map.getValue()
                        .stream()
                        .reduce(0, frequencyAccumulatorFunc, Integer::sum)));

        sentenceToWeight.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .skip(skipSentencesCount.applyAsInt(sentenceToWeight.size()))
                .limit(limitSentencesCount.applyAsInt(sentenceToWeight.size()))
                .forEach(x->System.out.println("Weight " + x.getValue() + ":  " + x.getKey()));
    }
}