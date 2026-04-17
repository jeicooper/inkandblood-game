package main;

import java.io.*;
import java.util.*;

public class NPCDatabase {

    GamePanel   gp;
    UserManager userManager;

    // NPC LIST
    // { id, name, nickname, role, chapter, quest, bio }
    private static final String[][] NPC_MASTER = {

            { "teodora",
                    "Teodora Alonzo Realonda",            "Nanay",
                    "Mother of Jose Rizal",      "Chapter 1", "Quest 1: Familya Rizal",
                    "A woman of remarkable intelligence and literary talent, Teodora Alonzo was Rizal's first teacher. She introduced him to books and poetry from a very young age, shaping the mind that would one day write Noli Me Tangere." },

            { "francisco",
                    "Francisco Engracio Rizal Mercado",           "Tatay",
                    "Father of Jose Rizal",      "Chapter 1", "Quest 1: Familya Rizal",
                    "A hardworking tenant farmer and one of the first in Laguna to establish a sugar mill. His dignity in the face of colonial hardship was not lost on young Jose." },

            { "concha",
                    "Conception Rizal",              "Concha",
                    "Youngest Sibling",          "Chapter 1", "Quest 1: Familya Rizal",
                    "Concepcion, called Concha, was the youngest of the Rizal siblings. Her life was tragically short — she passed away in childhood, leaving a grief in Jose that never fully healed." },

            { "saturnina",
                    "Saturnina Rizal",           "Neneng",
                    "Eldest Sister",             "Chapter 1", "Quest 1: Familya Rizal",
                    "Saturnina was a steady presence in the Rizal household. She kept a watchful eye on her younger brothers and sisters and later became a quiet supporter of Jose's reformist writings." },

            { "paciano",
                    "Paciano Rizal",             "Ute",
                    "Elder Brother & Protector", "Chapter 1", "Quest 1: Familya Rizal",
                    "Paciano was more than a brother — he was Jose's guardian and financier. He sacrificed his own academic ambitions so Jose could study, and later became a general in the Philippine Revolution." },

            { "narcisa",
                    "Narcisa Rizal",             "Sisa",
                    "Sibling",                   "Chapter 1", "Quest 1: Familya Rizal",
                    "Narcisa, nicknamed Sisa, was known for her warm and nurturing nature within the Rizal household." },

            { "olimpia",
                    "Olimpia Rizal",             "Ypia",
                    "Sibling",                   "Chapter 1", "Quest 1: Familya Rizal",
                    "Olimpia was spirited and strong-willed. She shared Jose's love of words and was one of his earliest companions in games of rhyme and storytelling." },

            { "lucia",
                    "Lucia Rizal",               "Lucing",
                    "Sibling",                   "Chapter 1", "Quest 1: Familya Rizal",
                    "Lucia was known for her gentle temperament. She was one of several sisters whose lives would later be upended by the political fallout from Jose's novels." },

            { "maria",
                    "Maria Rizal",               "Biang",
                    "Sibling",                   "Chapter 1", "Quest 1: Familya Rizal",
                    "Maria, nicknamed Biang, grew up in the lively Rizal home in Calamba. She remained close to Jose throughout his life, even as colonial pressures forced the family apart." },

            { "josefa",
                    "Josefa Rizal",              "Panggoy",
                    "Sibling",                   "Chapter 1", "Quest 1: Familya Rizal",
                    "Josefa was quiet and introspective. She outlived Jose by decades, carrying the memory of her brother's sacrifice for the rest of her long life." },

            { "trinidad",
                    "Trinidad Rizal",            "Trining",
                    "Sibling",                   "Chapter 1", "Quest 1: Familya Rizal",
                    "Trinidad was among the most devoted to Jose's memory. She was famously entrusted with his final poem, hidden inside a small alcohol lamp on the night before his execution." },

            { "soledad",
                    "Soledad Rizal",             "Choleng",
                    "Sibling",                   "Chapter 1", "Quest 1: Familya Rizal",
                    "Soledad, the youngest, was known as Choleng. Though young when Jose left for Europe, she grew up under the shadow and the pride of her famous brother." },

            { "jose",
                    "Jose Alberto",              "Uncle Jose",
                    "Artistic Uncle",            "Chapter 1", "Quest 2: Pangangaral ng mga Tiyo",
                    "A gifted painter who saw the spark of creativity in young Jose. He tasked his nephew with gathering art supplies as a lesson in patience and dedication." },

            { "manuel",
                    "Manuel Hidalgo",            "Uncle Manuel",
                    "Athletic Uncle",            "Chapter 1", "Quest 2: Pangangaral ng mga Tiyo",
                    "Uncle Manuel believed a sharp mind must live in a strong body. He designed a course through Calamba's fields, teaching Jose that perseverance must be practiced, not just preached." },

            { "gregorio",
                    "Gregorio Rizal",            "Uncle Gregorio",
                    "Scholar Uncle",             "Chapter 1", "Quest 2: Pangangaral ng mga Tiyo",
                    "A man of letters who saw in Jose a mind hungry for knowledge. He provided Jose with his first quill and notebook, igniting a lifelong love of writing." },

            { "ferrando",
                    "Fr. Pablo Ferrando",        "Father Ferrando",
                    "Ateneo Prefect",            "Chapter 2", "Quest 3: Ang Bagong Simula",
                    "The Prefect of Discipline at Ateneo Municipal de Manila. Initially dismissive of a boy from Calamba, Ferrando was won over after Rizal aced the academic quiz and personally awarded him the Sobresaliente medal." },

            { "burgos",
                    "Pedro Burgos",              "Senor Burgos",
                    "Reformist Priest",          "Chapter 2", "Quest 3: Ang Bagong Simula",
                    "A Filipino secular priest who advocated for the rights of native clergy. His execution in 1872 became a symbol of colonial injustice that deeply marked Rizal." },

            { "professor",
                    "Prof. Francisco",           "The Professor",
                    "Ateneo Professor",          "Chapter 2", "Quest 3: Ang Bagong Simula",
                    "A stern but fair professor who guided Jose through his first academic challenge at Ateneo. He believed that knowledge was the only weapon worth carrying." },

            { "interno",
                    "Classmate",                 "Interno",
                    "Ateneo Student",            "Chapter 2", "Quest 3: Ang Bagong Simula",
                    "A fellow interno at Ateneo who challenged Jose to prove himself before the whole class. His confidence in Rizal's abilities helped push Jose to study harder than ever." },

            { "mariano",
                    "Mariano Katigbak",          "Mariano",
                    "Fellow Student",            "Chapter 2", "Quest 4: Ang Kampeon ng Roma",
                    "A classmate and confidant at Ateneo who encouraged Jose to enter the discipline competitions. His rivalry with Jose was always friendly — each pushing the other to greater heights." },

            { "rector",
                    "Fr. Rector",                "The Rector",
                    "Ateneo Rector",             "Chapter 2", "Quest 4: Ang Kampeon ng Roma",
                    "The head of Ateneo Municipal de Manila who oversaw the discipline competitions. He recognized in Rizal an extraordinary scholar and presided over the awarding of his medals." },

            { "perfect",
                    "Prefect of Conduct",        "Prefect",
                    "Discipline Judge",          "Chapter 2", "Quest 4: Ang Kampeon ng Roma",
                    "The judge for the Conduct discipline. He evaluated not just behavior but character — testing whether Jose could hold composure under pressure." },

            { "casimiro",
                    "Fr. Casimiro",              "Casimiro",
                    "Painting Judge",            "Chapter 2", "Quest 4: Ang Kampeon ng Roma",
                    "The judge for the Painting discipline. He saw in Rizal's brushwork the same precision and passion that defined all of the young man's pursuits." },

            { "millano",
                    "Fr. Millano",               "Millano",
                    "French Language Judge",     "Chapter 2", "Quest 4: Ang Kampeon ng Roma",
                    "The French Language judge. He tested Jose's grasp of the language that would later help him navigate the salons of Europe." },

            { "desanctis",
                    "Fr. De Sanctis",            "De Sanctis",
                    "Rhetoric Judge",            "Chapter 2", "Quest 4: Ang Kampeon ng Roma",
                    "The Rhetoric judge. He challenged Jose to argue with both fire and structure — a skill Rizal would refine into the most powerful prose the Philippines had ever seen." },

            { "pedro",
                    "Pedro Paterno",             "Pedro",
                    "Patron of the Arts",        "Chapter 3", "Quest 5: Noli Me Tangere",
                    "A wealthy Filipino intellectual and writer in Madrid who provided moral and financial support to Rizal as he worked on his novel. He understood the weight of what Rizal was trying to do." },

            { "consuelo",
                    "Consuelo Ortiga",           "Consuelo",
                    "Friend in Madrid",          "Chapter 3", "Quest 5: Noli Me Tangere",
                    "A charming and intelligent woman in Madrid's Filipino expatriate circle. Her friendship helped sustain Rizal during the lonely, grueling months of writing Noli Me Tangere." },

            { "maximo",
                    "Maximo Viola",              "Maximo",
                    "Financier of the Noli",     "Chapter 3", "Quest 5: Noli Me Tangere",
                    "Maximo Viola was the man who made the Noli Me Tangere possible. When Rizal had no money left to print his novel, Viola provided the funds. He received the first signed copy in return." },

            { "paciano",
                    "Paciano Rizal",             "Kuya Paciano",
                    "Brother & Confidant",       "Chapter 3", "Quest 6: El Filibusterismo",
                    "Returning in a new capacity, Paciano was Jose's most trusted correspondent during the exile years. He relayed news from Calamba and channeled resources to his brother across the ocean." },
    };

    public Set<String> unlockedNPCs = new HashSet<>();
    public int dexSelectedIndex     = 0;
    public int dexScrollOffset      = 0;

    public NPCDatabase(GamePanel gp, UserManager userManager) {
        this.gp          = gp;
        this.userManager = userManager;
    }

    public void unlock(String dexId) {
        if (dexId == null || dexId.isEmpty()) return;
        unlockedNPCs.add(dexId);
    }

    public boolean isUnlocked(String id) { return unlockedNPCs.contains(id); }
    public int getTotalCount()            { return NPC_MASTER.length; }
    public int getUnlockedCount()         { return unlockedNPCs.size(); }

    public String getId(int i)       { return NPC_MASTER[i][0]; }
    public String getName(int i)     { return NPC_MASTER[i][1]; }
    public String getNickname(int i) { return NPC_MASTER[i][2]; }
    public String getRole(int i)     { return NPC_MASTER[i][3]; }
    public String getChapter(int i)  { return NPC_MASTER[i][4]; }
    public String getQuest(int i)    { return NPC_MASTER[i][5]; }
    public String getBio(int i)      { return NPC_MASTER[i][6]; }

    // File path: saves/<username>_npcdb.dat  (mirrors saves/<username>.dat)
    public void save() {
        if (!userManager.isLoggedIn()) return;
        File f = getFile();
        try {
            f.getParentFile().mkdirs();
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            for (String id : unlockedNPCs) {
                bw.write(id);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        reset();
        if (!userManager.isLoggedIn()) return;
        File f = getFile();
        if (!f.exists()) return;
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) unlockedNPCs.add(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteForUser(String username) {
        File f = new File("saves" + File.separator + username.trim() + "_npcdb.dat");
        if (f.exists()) f.delete();
    }

    public void reset() {
        unlockedNPCs.clear();
        dexSelectedIndex = 0;
        dexScrollOffset  = 0;
    }

    private File getFile() {
        String base = userManager.getSaveFile().getPath();
        return new File(base.replace(".dat", "_npcdb.dat"));
    }
}