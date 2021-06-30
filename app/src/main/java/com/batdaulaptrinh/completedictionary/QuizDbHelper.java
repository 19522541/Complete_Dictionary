package com.batdaulaptrinh.completedictionary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.batdaulaptrinh.completedictionary.QuizContract.*;

import java.util.ArrayList;
import java.util.List;


public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "EnglishQuiz.db";
    private static final int DATABASE_VERSION = 1;

    private static QuizDbHelper instance;

    private SQLiteDatabase db;

    private QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized QuizDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new QuizDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE " +
                CategoriesTable.TABLE_NAME + "( " +
                CategoriesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CategoriesTable.COLUMN_NAME + " TEXT " +
                ")";

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER_NR + " INTEGER, " +
                QuestionsTable.COLUMN_DIFFICULTY + " TEXT, " +
                QuestionsTable.COLUMN_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY(" + QuestionsTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
                CategoriesTable.TABLE_NAME + "(" + CategoriesTable._ID + ")" + "ON DELETE CASCADE" +
                ")";

        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillCategoriesTable();
        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CategoriesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void fillCategoriesTable() {
        Category c1 = new Category("MEANING");
        insertCategory(c1);
        Category c2 = new Category("SYNONYM");
        insertCategory(c2);
        Category c3 = new Category("PRONUNCIATION");
        insertCategory(c3);
    }

    public void addCategory(Category category) {
        db = getWritableDatabase();
        insertCategory(category);
    }

    public void addCategories(List<Category> categories) {
        db = getWritableDatabase();

        for (Category category : categories) {
            insertCategory(category);
        }
    }

    private void insertCategory(Category category) {
        ContentValues cv = new ContentValues();
        cv.put(CategoriesTable.COLUMN_NAME, category.getName());
        db.insert(CategoriesTable.TABLE_NAME, null, cv);
    }

    private void fillQuestionsTable() {
        Question q1 = new Question("An expert able to appreciate a field; especially in the fine arts",
                "CONNOISSEUR", "GRIP", "RIDDLE", 1,
                Question.DIFFICULTY_EASY, Category.MEANING);
        insertQuestion(q1);
        Question q2 = new Question("The act of grasping",
                "CONNOISSEUR", "GRIP", "RIDDLE", 2,
                Question.DIFFICULTY_EASY, Category.MEANING);
        insertQuestion(q2);
        Question q3 = new Question("Pierce with many holes",
                "CONNOISSEUR", "CONNOISSEUR", "CONNOISSEUR", 3,
                Question.DIFFICULTY_EASY, Category.MEANING);
        insertQuestion(q3);
        Question q4 = new Question("Keep or maintain in unaltered condition; cause to remain or last",
                "UPHOLD", "AMBIVALENT", "RIVAL", 1,
                Question.DIFFICULTY_EASY, Category.MEANING);
        insertQuestion(q4);
        Question q5 = new Question("Uncertain or unable to decide about what course to follow",
                "UPHOLD", "AMBIVALENT", "RIVAL", 2,
                Question.DIFFICULTY_EASY, Category.MEANING);
        insertQuestion(q5);
        Question q6 = new Question("Be equal to in quality or ability",
                "UPHOLD", "AMBIVALENT", "RIVAL", 3,
                Question.DIFFICULTY_EASY, Category.MEANING);
        insertQuestion(q6);
        Question q7 = new Question("Diffusing warmth and friendliness",
                "RIVAL", "CORDIAL", "PERSONALLY", 1,
                Question.DIFFICULTY_EASY, Category.MEANING);
        insertQuestion(q7);
        Question q8 = new Question("As yourself",
                "RIVAL", "CORDIAL", "PERSONALLY", 2,
                Question.DIFFICULTY_EASY, Category.MEANING);
        insertQuestion(q8);
        Question q9 = new Question("Immunity from arbitrary exercise of authority: political independence",
                "RIVAL", "CORDIAL", "PERSONALLY", 3,
                Question.DIFFICULTY_EASY, Category.MEANING);
        insertQuestion(q9);
        Question q10 = new Question("A way of doing something, especially a systematic way; implies an orderly logical arrangement (usually in steps)",
                "METHOD", "DECELERATE", "SNAKE", 1,
                Question.DIFFICULTY_EASY, Category.MEANING);
        insertQuestion(q10);
        Question q11 = new Question("Lose velocity; move more slowly",
                "METHOD", "DECELERATE", "SNAKE", 2,
                Question.DIFFICULTY_MEDIUM, Category.MEANING);
        insertQuestion(q11);
        Question q12 = new Question("Move smoothly and sinuously, like a snake",
                "METHOD", "DECELERATE", "SNAKE", 3,
                Question.DIFFICULTY_MEDIUM, Category.MEANING);
        insertQuestion(q12);
        Question q13 = new Question("Take the first step or steps in carrying out an action",
                "START", "AMAZE", "HISTORIC", 1,
                Question.DIFFICULTY_MEDIUM, Category.MEANING);
        insertQuestion(q13);
        Question q14 = new Question("Affect with wonder",
                "START", "AMAZE", "HISTORIC", 2,
                Question.DIFFICULTY_MEDIUM, Category.MEANING);
        insertQuestion(q14);
        Question q15 = new Question("Belonging to the past; of what is important or famous in the past",
                "START", "AMAZE", "HISTORIC", 3,
                Question.DIFFICULTY_MEDIUM, Category.MEANING);
        insertQuestion(q15);
        Question q16 = new Question("A person of subnormal intelligence",
                "RETARD", "MEMORIAL", "JOG", 1,
                Question.DIFFICULTY_MEDIUM, Category.MEANING);
        insertQuestion(q16);
        Question q17 = new Question("A recognition of meritorious service",
                "RETARD", "MEMORIAL", "JOG", 2,
                Question.DIFFICULTY_MEDIUM, Category.MEANING);
        insertQuestion(q17);
        Question q18 = new Question("A sharp change in direction",
                "RETARD", "MEMORIAL", "JOG", 3,
                Question.DIFFICULTY_MEDIUM, Category.MEANING);
        insertQuestion(q18);
        Question q19 = new Question("Having no beneficial use or incapable of functioning usefully",
                "USELESS", "PROFOUND", "APPLIANCE", 1,
                Question.DIFFICULTY_MEDIUM, Category.MEANING);
        insertQuestion(q19);
        Question q20 = new Question("Showing intellectual penetration or emotional depth",
                "USELESS", "PROFOUND", "APPLIANCE", 2,
                Question.DIFFICULTY_MEDIUM, Category.MEANING);
        insertQuestion(q20);
        Question q21 = new Question("A device or control that is very useful for a particular job",
                "USELESS", "PROFOUND", "APPLIANCE", 3,
                Question.DIFFICULTY_HARD, Category.MEANING);
        insertQuestion(q21);
        Question q22 = new Question("Quick or skillful or adept in action or thought",
                "ADROIT", "OWING", "FEAT", 1,
                Question.DIFFICULTY_HARD, Category.MEANING);
        insertQuestion(q22);
        Question q23 = new Question("Owed as a debt",
                "ADROIT", "OWING", "FEAT", 2,
                Question.DIFFICULTY_HARD, Category.MEANING);
        insertQuestion(q23);
        Question q24 = new Question("A notable achievement",
                "ADROIT", "OWING", "FEAT", 3,
                Question.DIFFICULTY_HARD, Category.MEANING);
        insertQuestion(q24);
        Question q25 = new Question("Lacking decisiveness of character; unable to act or decide quickly or firmly",
                "HESITANT", "CAFE", "STATIC", 1,
                Question.DIFFICULTY_HARD, Category.MEANING);
        insertQuestion(q25);
        Question q26 = new Question("A small restaurant where drinks and snacks are sold",
                "HESITANT", "CAFE", "STATIC", 2,
                Question.DIFFICULTY_HARD, Category.MEANING);
        insertQuestion(q26);
        Question q27 = new Question("Not in physical motion",
                "HESITANT", "CAFE", "STATIC", 3,
                Question.DIFFICULTY_HARD, Category.MEANING);
        insertQuestion(q27);
        Question q28 = new Question("Tending to deter",
                "DETERRENT", "GIVE", "TILE", 1,
                Question.DIFFICULTY_HARD, Category.MEANING);
        insertQuestion(q28);
        Question q29 = new Question("The elasticity of something that can be stretched and returns to its original length",
                "DETERRENT", "GIVE", "TILE", 2,
                Question.DIFFICULTY_HARD, Category.MEANING);
        insertQuestion(q29);
        Question q30 = new Question("A flat thin rectangular slab used to cover surfaces",
                "DETERRENT", "GIVE", "TILE", 3,
                Question.DIFFICULTY_HARD, Category.MEANING);
        insertQuestion(q30);
        Question q31 = new Question("Finally",
                "EVENTUALLY", "UNDERRATE", "SALARY", 1,
                Question.DIFFICULTY_EASY, Category.SYNONYM);
        insertQuestion(q31);
        Question q32 = new Question("Miscalculate",
                "EVENTUALLY", "UNDERRATE", "SALARY", 2,
                Question.DIFFICULTY_EASY, Category.SYNONYM);
        insertQuestion(q32);
        Question q33 = new Question("Annuity",
                "EVENTUALLY", "UNDERRATE", "SALARY", 3,
                Question.DIFFICULTY_EASY, Category.SYNONYM);
        insertQuestion(q33);
        Question q34 = new Question("Variable",
                "VARYING", "REDUCE", "SUFFICE", 1,
                Question.DIFFICULTY_EASY, Category.SYNONYM);
        insertQuestion(q34);
        Question q35 = new Question("Cut down on",
                "VARYING", "REDUCE", "SUFFICE", 2,
                Question.DIFFICULTY_EASY, Category.SYNONYM);
        insertQuestion(q35);
        Question q36 = new Question("Answer",
                "VARYING", "REDUCE", "SUFFICE", 3,
                Question.DIFFICULTY_EASY, Category.SYNONYM);
        insertQuestion(q36);
        Question q37 = new Question("Action",
                "MEDIUM", "EXECUTIVE", "SUPERVISE", 1,
                Question.DIFFICULTY_EASY, Category.SYNONYM);
        insertQuestion(q37);
        Question q38 = new Question("Bush administration",
                "MEDIUM", "EXECUTIVE", "SUPERVISE", 2,
                Question.DIFFICULTY_EASY, Category.SYNONYM);
        insertQuestion(q38);
        Question q39 = new Question("Build",
                "MEDIUM", "EXECUTIVE", "SUPERVISE", 3,
                Question.DIFFICULTY_EASY, Category.SYNONYM);
        insertQuestion(q39);
        Question q40 = new Question("Alcoholise",
                "TRANSFORM", "PARCH", "EBB", 1,
                Question.DIFFICULTY_EASY, Category.SYNONYM);
        insertQuestion(q40);
        Question q41 = new Question("Blow-dry",
                "TRANSFORM", "PARCH", "EBB", 2,
                Question.DIFFICULTY_MEDIUM, Category.SYNONYM);
        insertQuestion(q41);
        Question q42 = new Question("Air flow",
                "TRANSFORM", "PARCH", "EBB", 3,
                Question.DIFFICULTY_MEDIUM, Category.SYNONYM);
        insertQuestion(q42);
        Question q43 = new Question("Ankylose",
                "VEGETATE", "CONFIDENT", "CONVENIENCE", 1,
                Question.DIFFICULTY_MEDIUM, Category.SYNONYM);
        insertQuestion(q43);
        Question q44 = new Question("Capable",
                "VEGETATE", "CONFIDENT", "CONVENIENCE", 2,
                Question.DIFFICULTY_MEDIUM, Category.SYNONYM);
        insertQuestion(q44);
        Question q45 = new Question("Availability",
                "VEGETATE", "CONFIDENT", "CONVENIENCE", 3,
                Question.DIFFICULTY_MEDIUM, Category.SYNONYM);
        insertQuestion(q45);
        Question q46 = new Question("Acuteness",
                "BRAIN", "EXPENSIVE", "FRAGILE", 1,
                Question.DIFFICULTY_MEDIUM, Category.SYNONYM);
        insertQuestion(q46);
        Question q47 = new Question("Big-ticket",
                "BRAIN", "EXPENSIVE", "FRAGILE", 2,
                Question.DIFFICULTY_MEDIUM, Category.SYNONYM);
        insertQuestion(q47);
        Question q48 = new Question("Breakable",
                "BRAIN", "EXPENSIVE", "FRAGILE", 3,
                Question.DIFFICULTY_MEDIUM, Category.SYNONYM);
        insertQuestion(q48);
        Question q49 = new Question("Difficult",
                "DEMANDING", "RENDER", "HISTORICAL", 1,
                Question.DIFFICULTY_MEDIUM, Category.SYNONYM);
        insertQuestion(q49);
        Question q50 = new Question("Acknowledge",
                "DEMANDING", "RENDER", "HISTORICAL", 2,
                Question.DIFFICULTY_MEDIUM, Category.SYNONYM);
        insertQuestion(q50);
        Question q51 = new Question("Diachronic",
                "DEMANDING", "RENDER", "HISTORICAL", 3,
                Question.DIFFICULTY_HARD, Category.SYNONYM);
        insertQuestion(q51);
        Question q52 = new Question("Alarming",
                "UGLY", "ENTHUSIASTIC", "ARTICULATE", 1,
                Question.DIFFICULTY_HARD, Category.SYNONYM);
        insertQuestion(q52);
        Question q53 = new Question("Ardent",
                "UGLY", "ENTHUSIASTIC", "ARTICULATE", 2,
                Question.DIFFICULTY_HARD, Category.SYNONYM);
        insertQuestion(q53);
        Question q54 = new Question("Articulated",
                "UGLY", "ENTHUSIASTIC", "ARTICULATE", 3,
                Question.DIFFICULTY_HARD, Category.SYNONYM);
        insertQuestion(q54);
        Question q55 = new Question("Burdenless",
                "UNENCUMBERED", "INSATIABLE", "MASTERPIECE", 1,
                Question.DIFFICULTY_HARD, Category.SYNONYM);
        insertQuestion(q55);
        Question q56 = new Question("Insatiate",
                "UNENCUMBERED", "INSATIABLE", "MASTERPIECE", 2,
                Question.DIFFICULTY_HARD, Category.SYNONYM);
        insertQuestion(q56);
        Question q57 = new Question("Attainment",
                "UNENCUMBERED", "INSATIABLE", "MASTERPIECE", 3,
                Question.DIFFICULTY_HARD, Category.SYNONYM);
        insertQuestion(q57);
        Question q58 = new Question("Bloody",
                "SUBMERGE", "SYNERGY", "ARTICULATION", 1,
                Question.DIFFICULTY_HARD, Category.SYNONYM);
        insertQuestion(q58);
        Question q59 = new Question("Absorption",
                "SUBMERGE", "SYNERGY", "ARTICULATION", 2,
                Question.DIFFICULTY_HARD, Category.SYNONYM);
        insertQuestion(q59);
        Question q60 = new Question("Annexa",
                "SUBMERGE", "SYNERGY", "ARTICULATION", 3,
                Question.DIFFICULTY_HARD, Category.SYNONYM);
        insertQuestion(q60);
        Question q61 = new Question("Choose the difference",
                "Justice", "Campus", "Culture     ", 2,
                Question.DIFFICULTY_EASY, Category.PRONUNCIATION);
        insertQuestion(q61);
        Question q62 = new Question("Choose the difference",
                "work     ", "form     ", "stork     ", 1,
                Question.DIFFICULTY_EASY, Category.PRONUNCIATION);
        insertQuestion(q62);
        Question q63 = new Question("Choose the difference",
                "eleven     ", "elephant     ", "examine     ", 2,
                Question.DIFFICULTY_EASY, Category.PRONUNCIATION);
        insertQuestion(q63);
        Question q64 = new Question("Choose the difference",
                "pressure     ", "possession     ", "assist", 3,
                Question.DIFFICULTY_EASY, Category.PRONUNCIATION);
        insertQuestion(q64);
        Question q65 = new Question("Choose the difference",
                "species    ", "invent     ", "medicine     ", 1,
                Question.DIFFICULTY_EASY, Category.PRONUNCIATION);
        insertQuestion(q65);
        Question q66 = new Question("Choose the difference",
                "deal     ", "teach     ", "break     ", 3,
                Question.DIFFICULTY_EASY, Category.PRONUNCIATION);
        insertQuestion(q66);
        Question q67 = new Question("Choose the difference",
                "supported    ", "approached     ", "noticed     ", 1,
                Question.DIFFICULTY_EASY, Category.PRONUNCIATION);
        insertQuestion(q67);
        Question q68 = new Question("Choose the difference",
                "justice     ", "campus     ", "culture     ", 2,
                Question.DIFFICULTY_EASY, Category.PRONUNCIATION);
        insertQuestion(q68);
        Question q69 = new Question("Choose the difference",
                "face     ", "page     ", "map", 3,
                Question.DIFFICULTY_EASY, Category.PRONUNCIATION);
        insertQuestion(q69);
        Question q70 = new Question("Choose the difference",
                "dropped     ", "matched", "joined", 3,
                Question.DIFFICULTY_EASY, Category.PRONUNCIATION);
        insertQuestion(q70);
        Question q71 = new Question("Choose the difference",
                "walk", "call", "take", 3,
                Question.DIFFICULTY_MEDIUM, Category.PRONUNCIATION);
        insertQuestion(q71);
        Question q72 = new Question("Choose the difference",
                "find     ", "think     ", "drive     ", 2,
                Question.DIFFICULTY_MEDIUM, Category.PRONUNCIATION);
        insertQuestion(q72);
        Question q73 = new Question("Choose the difference",
                "hook     ", "stood     ", "tool", 3,
                Question.DIFFICULTY_MEDIUM, Category.PRONUNCIATION);
        insertQuestion(q73);
        Question q74 = new Question("Choose the difference",
                "canal     ", "journal     ", "refusal     ", 1,
                Question.DIFFICULTY_MEDIUM, Category.PRONUNCIATION);
        insertQuestion(q74);
        Question q75 = new Question("Choose the difference",
                "definition     ", "suggestion     ", "situation     ", 2,
                Question.DIFFICULTY_MEDIUM, Category.PRONUNCIATION);
        insertQuestion(q75);
        Question q76 = new Question("Choose the difference",
                "exported     ", "enjoyed     ", "existed     ", 2,
                Question.DIFFICULTY_MEDIUM, Category.PRONUNCIATION);
        insertQuestion(q76);
        Question q77 = new Question("Choose the difference",
                "Wednesday     ", "handsome     ", "dependent     ", 3,
                Question.DIFFICULTY_MEDIUM, Category.PRONUNCIATION);
        insertQuestion(q77);
        Question q78 = new Question("Choose the difference",
                "ocean     ", "official     ", "sincere     ", 3,
                Question.DIFFICULTY_MEDIUM, Category.PRONUNCIATION);
        insertQuestion(q78);
        Question q79 = new Question("Choose the difference",
                "missed     ", "filled     ", "published     ", 2,
                Question.DIFFICULTY_MEDIUM, Category.PRONUNCIATION);
        insertQuestion(q79);
        Question q80 = new Question("Choose the difference",
                "walked    ", "involved     ", "missed     ", 2,
                Question.DIFFICULTY_MEDIUM, Category.PRONUNCIATION);
        insertQuestion(q80);
        Question q81 = new Question("Choose the difference",
                "collect     ", "operate     ", "hobby     ", 1,
                Question.DIFFICULTY_HARD, Category.PRONUNCIATION);
        insertQuestion(q81);
        Question q82 = new Question("Choose the difference",
                "missed     ", "washed     ", "naked", 3,
                Question.DIFFICULTY_HARD, Category.PRONUNCIATION);
        insertQuestion(q82);
        Question q83 = new Question("Choose the difference",
                "climate     ", "liveable     ", "city     ", 1,
                Question.DIFFICULTY_HARD, Category.PRONUNCIATION);
        insertQuestion(q83);
        Question q84 = new Question("Choose the difference",
                "solar    ", "infrastructure     ", "designer     ", 3,
                Question.DIFFICULTY_HARD, Category.PRONUNCIATION);
        insertQuestion(q84);
        Question q85 = new Question("Choose the difference",
                "objection     ", "obey     ", "impose     ", 3,
                Question.DIFFICULTY_HARD, Category.PRONUNCIATION);
        insertQuestion(q85);
        Question q86 = new Question("Choose the difference",
                "pierce     ", "advice     ", "activity", 3,
                Question.DIFFICULTY_HARD, Category.PRONUNCIATION);
        insertQuestion(q86);
        Question q87 = new Question("Choose the difference",
                "stood     ", "wood     ", "food", 3,
                Question.DIFFICULTY_HARD, Category.PRONUNCIATION);
        insertQuestion(q87);
        Question q88 = new Question("Choose the difference",
                "books     ", "pens", "shirts", 2,
                Question.DIFFICULTY_HARD, Category.PRONUNCIATION);
        insertQuestion(q88);
        Question q89 = new Question("Choose the difference",
                "village     ", "luggage     ", "engage     ", 3,
                Question.DIFFICULTY_HARD, Category.PRONUNCIATION);
        insertQuestion(q89);
        Question q90 = new Question("Choose the difference",
                "swallowed     ", "practiced     ", "finished     ", 1,
                Question.DIFFICULTY_HARD, Category.PRONUNCIATION);
        insertQuestion(q90);


    }

    public void addQuestion(Question question) {
        db = getWritableDatabase();
        insertQuestion(question);
    }

    public void addQuestions(List<Question> questions) {
        db = getWritableDatabase();

        for (Question question : questions) {
            insertQuestion(question);
        }
    }

    private void insertQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        cv.put(QuestionsTable.COLUMN_DIFFICULTY, question.getDifficulty());
        cv.put(QuestionsTable.COLUMN_CATEGORY_ID, question.getCategoryID());
        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }

    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + CategoriesTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(c.getInt(c.getColumnIndex(CategoriesTable._ID)));
                category.setName(c.getString(c.getColumnIndex(CategoriesTable.COLUMN_NAME)));
                categoryList.add(category);
            } while (c.moveToNext());
        }

        c.close();
        return categoryList;
    }

    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionsTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }

    public ArrayList<Question> getQuestions(int categoryID, String difficulty) {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();

        String selection = QuestionsTable.COLUMN_CATEGORY_ID + " = ? " +
                " AND " + QuestionsTable.COLUMN_DIFFICULTY + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(categoryID), difficulty};

        Cursor c = db.query(
                QuestionsTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionsTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }
}
