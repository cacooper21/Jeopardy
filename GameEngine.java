public class GameEngine {
    private String[] questions;

    public GameEngine(){
        Scanner sc = new Scanner(new File("./questions.txt"));
        List<String> lines = new ArrayList<String>();
        while (sc.hasNextLine()) {
        lines.add(sc.nextLine());
        }
            questions = lines.toArray(new String[0]);
    }
}