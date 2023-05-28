package bsu.rfe.java.teacher;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;
@SuppressWarnings("serial")
public class Field extends JPanel {
                                                                        // Флаг приостановленности движения
    private boolean paused;

                                                                                    // Динамический список скачущих мячей
    private ArrayList<BouncingBall> balls = new ArrayList<BouncingBall>(10);
                                                                                    // Класс таймер отвечает за регулярную генерацию событий ActionEvent
                                                                                //// При создании его экземпляра используется анонимный класс,
                                                                                // реализующий интерфейс ActionListener
    private Timer repaintTimer = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
                                                                            // Задача обработчика события ActionEvent - перерисовка окна
            repaint();
        }
    });
                                                    // Конструктор класса BouncingBall
    public Field() {
                                            // Установить цвет заднего фона белым
        setBackground(Color.WHITE);
                                                // Запустить таймер
        repaintTimer.start();
    }
                                                    // Унаследованный от JPanel метод перерисовки компонента
    public void paintComponent(Graphics g) {
                                                    // Вызвать версию метода, унаследованную от предка
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;
                                                                // Последовательно запросить прорисовку от всех мячей из списка
        for (BouncingBall ball: balls) {
            ball.paint(canvas);
        }
    }
                                                          // Метод добавления нового мяча в список
    public void addBall() {
                                                                            //Заключается в добавлении в список нового экземпляра BouncingBall
                                                                            // Всю инициализацию положения, скорости, размера, цвета
                                                                            // BouncingBall выполняет сам в конструкторе
        balls.add(new BouncingBall(this));
    }
                                                                            // Метод синхронизированный, т.е. только один поток может
                                                                        // одновременно быть внутри
    public synchronized void pause() {
                                                                                    // Включить режим паузы
        paused = true;
    }
                                                                            // Метод синхронизированный, т.е. только один поток может
                                                                        // одновременно быть внутри
    public synchronized void resume() {
                                                                        // Выключить режим паузы
        paused = false;
                                                                    // Будим все ожидающие продолжения потоки
        notifyAll();
    }
                                                                    // Синхронизированный метод проверки, может ли мяч двигаться
                                                                // (не включен ли режим паузы?)
    public synchronized void canMove(BouncingBall ball) throws
            InterruptedException {
        if (paused) {
                                                                    // Если режим паузы включен, то поток, зашедший
                                                                    // внутрь данного метода, засыпает
            wait();
        }
    }

    public void pauseBalls(List<BouncingBall> balls) {
        for (BouncingBall ball : balls) {
            ball.pausedBallR30(true);
        }
    }

    public void resumeBalls(List<BouncingBall> balls) {
        for (BouncingBall ball : balls) {
            ball.pausedBallR30(false);
        }
    }

    public void pause30() {
        List<BouncingBall> bigBalls = new ArrayList<>();
        for (BouncingBall ball : balls) {
            if (ball.getRadius() > 30) {
                bigBalls.add(ball);
            }
        }
        pauseBalls(bigBalls);
    }

    public ArrayList<BouncingBall> getBalls() {
        return balls;
    }

    public void setBalls(ArrayList<BouncingBall> balls) {
        this.balls = balls;
    }
}
