package tko.imajedi.tp1;

import android.nfc.Tag;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final int ROW_COUNT = 10;
    public static final int COL_COUNT = 10;

    private int[] data = new int[100];
    private int BombCount = 10;
    private static Random rand = new Random();
    private boolean[] BombMap = new boolean[100];
    private int[] IsShowMap = new int[100];//0 non révélé, 1 drapeau, 2 révélé

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridLayout grid = findViewById(R.id.gridlayout);

        for(int i = 0; i<BombMap.length-1; i++)
        {
            BombMap[i] = false;
        }

        for(int i = 0; i<BombCount; i++)
        {
            int newBomb = rand.nextInt(100);
            while (BombMap[newBomb] == true)
            {
                newBomb = rand.nextInt(100);
            }
            BombMap[newBomb] = true;
        }

        for(int i = 0; i<BombCount; i++)
        {
            IsShowMap[i] = 0;
        }

        for(int i = 0; i<ROW_COUNT; i++)
        {
            for(int j = 0; j<COL_COUNT; j++) {
                final int x = j;
                final int y = i;
                int index = x + y * 10;

                Button button = (Button) grid.getChildAt(index);

                Log.d("Debug", "button=" + button + ", x=" + x + ", y=" + y + ", index=" + index);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View v){ OnCellClicked(x, y); }
                });
            }
        }
    }

    /*public static final int MINE = 0;
    public static final int EXPOSED = 1;*/

    /*public boolean IsBitOn(int index)
    {
        int value = 1 << EXPOSED;
        return (data[index] & value) == value;
    }*/

    public void TurnBitOn(int index, int EXPOSED)
    {
        int value = 1 << EXPOSED;
        data[index] |= value;
    }

    public void Reveal(int x, int y)
    {
        int index = CheckIndex(x, y);
        int bombCount;
        if(IsShowMap[index] == 0)
        {
            if(BombMap[index] == false)
            {
                bombCount = CheckAround(x, y);
                Log.d("Debug", ""+bombCount);
            }
            else
            {
                Log.d("Debug", "Hit A Bomb");
                //GameOver;
            }
        }
        else
        {
            //GameOver();
        }
    }

    public int CheckAround(int x, int y)
    {
        int bombAround = 0;

        if(x>0 && x<9)
        {
            if(y>0 && y<9)
            {
                bombAround += CheckContaint(x-1,y-1);
                bombAround += CheckContaint(x-1,y);
                bombAround += CheckContaint(x-1,y+1);
                bombAround += CheckContaint(x,y-1);
                bombAround += CheckContaint(x,y+1);
                bombAround += CheckContaint(x+1,y-1);
                bombAround += CheckContaint(x+1,y);
                bombAround += CheckContaint(x+1,y+1);
            }
            else if(y>0)
            {
                bombAround += CheckContaint(x-1,y-1);
                bombAround += CheckContaint(x-1,y);
                bombAround += CheckContaint(x,y-1);
                bombAround += CheckContaint(x+1,y-1);
                bombAround += CheckContaint(x+1,y);

            }
            else if(y<9)
            {
                bombAround += CheckContaint(x-1,y);
                bombAround += CheckContaint(x-1,y+1);
                bombAround += CheckContaint(x,y+1);
                bombAround += CheckContaint(x+1,y);
                bombAround += CheckContaint(x+1,y+1);
            }
        }
        else if(x>0)
        {
            if(y>0 && y<9)
            {
                bombAround += CheckContaint(x-1,y-1);
                bombAround += CheckContaint(x-1,y);
                bombAround += CheckContaint(x-1,y+1);
                bombAround += CheckContaint(x,y-1);
                bombAround += CheckContaint(x,y+1);
            }
            else if(y>0)
            {
                bombAround += CheckContaint(x-1,y-1);
                bombAround += CheckContaint(x-1,y);
                bombAround += CheckContaint(x,y-1);
            }
            else if(y<9)
            {
                bombAround += CheckContaint(x-1,y);
                bombAround += CheckContaint(x-1,y+1);
                bombAround += CheckContaint(x,y+1);
            }
        }
        else if(x<9)
        {
            if(y>0 && y<9)
            {
                bombAround += CheckContaint(x,y-1);
                bombAround += CheckContaint(x,y+1);
                bombAround += CheckContaint(x+1,y-1);
                bombAround += CheckContaint(x+1,y);
                bombAround += CheckContaint(x+1,y+1);
            }
            else if(y>0)
            {
                bombAround += CheckContaint(x,y-1);
                bombAround += CheckContaint(x+1,y-1);
                bombAround += CheckContaint(x+1,y);
            }
            else if(y<9)
            {
                bombAround += CheckContaint(x,y+1);
                bombAround += CheckContaint(x+1,y);
                bombAround += CheckContaint(x+1,y+1);
            }
        }

        return bombAround;
    }

    public int CheckIndex(int x, int y)
    {
        int index = x + (y * 10);
        return index;
    }

    public int CheckContaint(int x, int y)
    {
        int bombCount;
        if(BombMap[CheckIndex(x, y)] == false)
        {
            bombCount = 0;
        }
        else
        {
            bombCount = 1;
        }
        return bombCount;
    }

    public void TurnBitOff(int index, int EXPOSED)
    {
        int value = 1 << EXPOSED;
        data[index] &= ~value;
    }

    public void OnCellClicked(int x, int y)
    {
        Log.d(TAG, "OnCellClicked(" + x + ", " + y +")");
        Reveal(x, y);
        //Refresh()
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
