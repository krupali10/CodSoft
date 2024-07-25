import java.util.*;

class Question {
    String questionText;
    List<String> options;
    int correctAnswer;

    public Question(String questionText, List<String> options, int correctAnswer) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }
}

class Quiz {
    private List<Question> questions;
    private int score;
    private Map<Integer, Boolean> results;

    public Quiz(List<Question> questions) {
        this.questions = questions;
        this.score = 0;
        this.results = new HashMap<>();
    }

    public void startQuiz() {
        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            System.out.println("Question " + (i + 1) + ": " + question.questionText);
            for (int j = 0; j < question.options.size(); j++) {
                System.out.println((j + 1) + ". " + question.options.get(j));
            }

            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    System.out.println("\nTime's up! Moving to next question.");
                    synchronized (scanner) {
                        scanner.notify();
                    }
                }
            };

            timer.schedule(task, 10000); // 10 seconds for each question

            int answer = -1;
            synchronized (scanner) {
                try {
                    if (scanner.hasNextInt()) {
                        answer = scanner.nextInt();
                    }
                    scanner.wait(10000); // Wait for up to 10 seconds for user input
                } catch (InterruptedException e) {
                    System.out.println("Interrupted!");
                }
            }
            timer.cancel();

            if (answer == question.correctAnswer) {
                score++;
                results.put(i + 1, true);
            } else {
                results.put(i + 1, false);
            }
        }

        displayResults();
    }

    private void displayResults() {
        System.out.println("\nQuiz Completed!");
        System.out.println("Your Score: " + score + "/" + questions.size());
        for (Map.Entry<Integer, Boolean> entry : results.entrySet()) {
            System.out.println("Question " + entry.getKey() + ": " + (entry.getValue() ? "Correct" : "Incorrect"));
        }
    }
}

public class QuizApp {
    public static void main(String[] args) {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("Which of the following is not a Java features?",
                Arrays.asList("1. Dynamic", "2. Architecture Neutral", "3. Use of pointers", "4. Object-oriented"), 3));
        questions.add(new Question("JDK stands for ____.?",
                Arrays.asList("1. Java development kit", "2. Java deployment kit", "3. JavaScript deployment kit", "4. None of these"), 1));
        questions.add(new Question("What is the entry point of a program in Java??",
                Arrays.asList("1. main class", "2. The first line of code", "3. Last line of code", "4. main() method "), 4));

        Quiz quiz = new Quiz(questions);
        quiz.startQuiz();
    }
}