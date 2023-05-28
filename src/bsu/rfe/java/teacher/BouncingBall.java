package bsu.rfe.java.teacher;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class BouncingBall implements Runnable {
    // Максимальный радиус, который может иметь мяч
    private static final int MAX_RADIUS = 40;
    // Минимальный радиус, который может иметь мяч
    private static final int MIN_RADIUS = 3;
    // Максимальная скорость, с которой может летать мяч
    private static final int MAX_SPEED = 15;
    private Field field;
    private int radius;
    private Color color;

    private boolean paused = false;
    // Текущие координаты мяча
    private double x;
    private double y;
    // Вертикальная и горизонтальная компонента скорости
    private int speed;

    private double speedXBeforePaused;
    private double speedYBeforePaused;
    private double speedX;
    private double speedY;

    // Конструктор класса BouncingBall
    public BouncingBall(Field field) {
// Необходимо иметь ссылку на поле, по которому прыгает мяч,
// чтобы отслеживать выход за его пределы
// через getWidth(), getHeight()
        this.field = field;
// Радиус мяча случайного размера
        radius = new Double(Math.random() * (MAX_RADIUS -
                MIN_RADIUS)).intValue() + MIN_RADIUS;
// Абсолютное значение скорости зависит от диаметра мяча,
// чем он больше, тем медленнее
        speed = new Double(Math.round(5 * MAX_SPEED / radius)).intValue();
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
// Начальное направление скорости тоже случайно,
// угол в пределах от 0 до 2PI
        double angle = Math.random() * 2 * Math.PI;

// Вычисляются горизонтальная и вертикальная компоненты скорости
        speedX = 3 * Math.cos(angle);
        speedY = 3 * Math.sin(angle);
// Цвет мяча выбирается случайно
        color = new Color((float) Math.random(), (float) Math.random(),
                (float) Math.random());
// Начальное положение мяча случайно
        x = Math.random() * (field.getSize().getWidth() - 2 * radius) + radius;
        y = Math.random() * (field.getSize().getHeight() - 2 * radius) + radius;
// Создаѐм новый экземпляр потока, передавая аргументом
// ссылку на класс, реализующий Runnable (т.е. на себя)
        Thread thisThread = new Thread(this);
// Запускаем поток
        thisThread.start();
    }

    public int getRadius() {
        return radius;
    }

    // Метод run() исполняется внутри потока. Когда он завершает работу,
// то завершается и поток
    public void run() {
        try {
// Крутим бесконечный цикл, т.е. пока нас не прервут,
// мы не намерены завершаться
            while (true) {
// Синхронизация потоков на самом объекте поля
// Если движение разрешено - управление будет
// возвращено в метод
// В противном случае - активный поток заснѐт
                field.canMove(this);
                if (x + speedX <= radius + 50) {
// Достигли левой стенки, отскакиваем право
                    speedX = -speedX;
                    x = radius + 50;
                } else if (x + speedX >= field.getWidth() - radius - 50) {
// Достигли правой стенки, отскок влево
                    speedX = -speedX;
                    x = new Double(field.getWidth() - radius).intValue() - 50;
                } else if (y + speedY <= radius + 50) {
// Достигли верхней стенки
                    speedY = -speedY;
                    y = radius + 50;
                } else if (y + speedY >= field.getHeight() - radius - 50) {
// Достигли нижней стенки
                    speedY = -speedY;
                    y = new Double(field.getHeight() - radius).intValue() - 50;
                } else {
// Просто смещаемся
                    x += speedX;
                    y += speedY;
                }
// Засыпаем на X миллисекунд, где X определяется
// исходя из скорости
// Скорость = 1 (медленно), засыпаем на 15 мс.
// Скорость = 15 (быстро), засыпаем на 1 мс.
                Thread.sleep(16 - speed);
            }
        } catch (InterruptedException ex) {
// Если нас прервали, то ничего не делаем
// и просто выходим (завершаемся)
        }
    }

    // Метод прорисовки самого себя
    public void paint(Graphics2D canvas) {
        canvas.setColor(color);
        canvas.setPaint(color);
        Ellipse2D.Double ball = new Ellipse2D.Double(x - radius, y - radius,
                2 * radius, 2 * radius);
        canvas.draw(ball);
        canvas.fill(ball);
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public double getSpeedX() {
        return speedX;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    public boolean isPaused() {
        return paused;
    }

    public void pausedBallR30(boolean paused) {
        this.paused = paused;
        speedXBeforePaused = speedX;
        speedYBeforePaused = speedY;
        speedX = 0;
        speedY = 0;
    }

    public void resumeBallR30(boolean paused) {
        this.paused = paused;
        speedX = speedXBeforePaused;
        speedY = speedYBeforePaused;
    }
}
