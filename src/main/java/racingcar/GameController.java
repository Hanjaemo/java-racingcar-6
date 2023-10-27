package racingcar;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import racingcar.domain.Car;
import racingcar.domain.Referee;
import racingcar.view.InputView;
import racingcar.view.OutputView;

public class GameController {

    private final RandomNumberGenerator numberGenerator;

    public GameController(RandomNumberGenerator numberGenerator) {
        this.numberGenerator = numberGenerator;
    }

    public void run() {
        // 게임 초기 설정
        List<Car> cars = createCars();
        int numberOfAttempts = InputView.readNumberOfAttempts();

        // 게임 진행
        playRacing(cars, numberOfAttempts);
        List<String> winners = judge(cars);
        OutputView.printWinners(winners);
    }

    private List<Car> createCars() {
        List<String> carNames = InputView.readCarNames();
        return carNames.stream()
                .map(Car::new)
                .toList();
    }

    private void playRacing(List<Car> cars, int numberOfAttempts) {
        OutputView.printRacingResultMessage();
        while (numberOfAttempts > 0) {
            List<Integer> forwardCounts = getForwardCounts(cars);
            List<String> carNames = getCarNames(cars);
            OutputView.printRacingResult(carNames, forwardCounts);
            System.out.print("\n");
            numberOfAttempts--;
        }
    }

    private List<Integer> getForwardCounts(List<Car> cars) {
        List<Integer> forwardCounts = new ArrayList<>();
        for (Car car : cars) {
            int forwardCount = car.moveForward(numberGenerator.generate());
            forwardCounts.add(forwardCount);
        }
        return forwardCounts;
    }

    private List<String> getCarNames(List<Car> cars) {
        return cars.stream()
                .map(Car::getName)
                .toList();
    }

    private List<String> judge(List<Car> cars) {
        Referee referee = new Referee();
        return getWinners(referee, cars);
    }

    private List<String> getWinners(Referee referee, List<Car> cars) {
        return referee.judge(cars)
                .stream()
                .map(Car::getName)
                .collect(Collectors.toList());
    }
}
