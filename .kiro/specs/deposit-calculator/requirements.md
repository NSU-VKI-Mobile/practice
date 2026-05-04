# Requirements Document

## Introduction

Android-приложение «Расчёт вкладов» предназначено для расчёта доходности банковского вклада. Пользователь вводит параметры вклада в два этапа (два отдельных экрана), получает итоговый расчёт и может сохранить его в историю. Приложение реализует архитектуру MVVM, хранит историю расчётов в локальной базе данных Room и соответствует принципам Material Design.

## Glossary

- **App** — Android-приложение «Расчёт вкладов»
- **MainScreen** — главный экран приложения с тремя кнопками навигации
- **InputStep1Screen** — экран первого этапа ввода (стартовый взнос и срок вклада)
- **InputStep2Screen** — экран второго этапа ввода (процентная ставка и ежемесячное пополнение)
- **ResultScreen** — экран отображения результата расчёта
- **HistoryScreen** — экран списка сохранённых расчётов
- **DetailsScreen** — экран детальной информации по сохранённому расчёту
- **InputViewModel** — ViewModel, управляющая состоянием ввода данных на Step1 и Step2
- **ResultViewModel** — ViewModel, управляющая состоянием экрана результата
- **HistoryViewModel** — ViewModel, управляющая списком истории расчётов
- **DetailsViewModel** — ViewModel, управляющая детальным просмотром расчёта
- **DepositRepository** — репозиторий, обеспечивающий доступ к данным через DAO
- **AppDatabase** — Singleton Room Database для хранения расчётов
- **DepositDao** — Data Access Object для операций с таблицей расчётов
- **DepositCalculation** — сущность Room, представляющая один сохранённый расчёт
- **Validator** — компонент валидации пользовательского ввода
- **Calculator** — компонент вычисления итоговой суммы и начисленных процентов
- **InitialAmount** — стартовый взнос (обязательное поле, положительное число)
- **PeriodMonths** — срок вклада в месяцах (обязательное поле, целое положительное число)
- **InterestRate** — годовая процентная ставка, определяемая автоматически по сроку вклада
- **MonthlyTopUp** — ежемесячное пополнение (необязательное поле, неотрицательное число)
- **FinalAmount** — итоговая сумма вклада по окончании срока
- **InterestEarned** — сумма начисленных процентов за весь срок

---

## Requirements

### Requirement 1: Главный экран

**User Story:** Как пользователь, я хочу видеть главный экран с понятной навигацией, чтобы быстро перейти к нужному действию.

#### Acceptance Criteria

1. THE App SHALL отображать на MainScreen заголовок «Расчёт вкладов» в шапке экрана.
2. THE MainScreen SHALL содержать кнопку «Рассчитать», при нажатии на которую выполняется переход на InputStep1Screen.
3. THE MainScreen SHALL содержать кнопку «История расчётов», при нажатии на которую выполняется переход на HistoryScreen.
4. THE MainScreen SHALL содержать кнопку «Закрыть приложение», при нажатии на которую App завершает работу.

---

### Requirement 2: Первый этап ввода — основные параметры

**User Story:** Как пользователь, я хочу ввести стартовый взнос и срок вклада, чтобы перейти к следующему шагу расчёта.

#### Acceptance Criteria

1. THE InputStep1Screen SHALL содержать текстовое поле для ввода InitialAmount с числовой клавиатурой.
2. THE InputStep1Screen SHALL содержать текстовое поле для ввода PeriodMonths с числовой клавиатурой.
3. THE InputStep1Screen SHALL содержать кнопку «В начало», при нажатии на которую выполняется переход на MainScreen.
4. THE InputStep1Screen SHALL содержать кнопку «Далее», при нажатии на которую выполняется переход на InputStep2Screen.
5. WHEN пользователь нажимает «Далее» и поле InitialAmount пустое или равно нулю, THE Validator SHALL отображать сообщение об ошибке под полем InitialAmount и блокировать переход на InputStep2Screen.
6. WHEN пользователь нажимает «Далее» и поле PeriodMonths пустое или равно нулю, THE Validator SHALL отображать сообщение об ошибке под полем PeriodMonths и блокировать переход на InputStep2Screen.
7. WHILE пользователь находится на InputStep1Screen, THE InputViewModel SHALL сохранять введённые значения InitialAmount и PeriodMonths при повороте экрана.

---

### Requirement 3: Второй этап ввода — дополнительные параметры

**User Story:** Как пользователь, я хочу видеть автоматически подобранную процентную ставку и при необходимости указать ежемесячное пополнение, чтобы получить точный расчёт.

#### Acceptance Criteria

1. THE InputStep2Screen SHALL отображать выпадающий список (Spinner) со значением InterestRate, автоматически выбранным по следующему правилу: если PeriodMonths < 6 — 15%, если 6 ≤ PeriodMonths < 12 — 10%, если PeriodMonths ≥ 12 — 5%.
2. THE InputStep2Screen SHALL позволять пользователю вручную изменить значение InterestRate, выбрав другое значение из выпадающего списка.
3. THE InputStep2Screen SHALL содержать текстовое поле для ввода MonthlyTopUp с числовой клавиатурой; поле является необязательным.
4. THE InputStep2Screen SHALL содержать кнопку «Назад», при нажатии на которую выполняется переход на InputStep1Screen с сохранением ранее введённых значений.
5. THE InputStep2Screen SHALL содержать кнопку «Рассчитать», при нажатии на которую THE Calculator SHALL вычислить FinalAmount и InterestEarned и выполнить переход на ResultScreen.
6. WHILE пользователь находится на InputStep2Screen, THE InputViewModel SHALL сохранять выбранное значение InterestRate и введённое значение MonthlyTopUp при повороте экрана.
7. IF поле MonthlyTopUp содержит отрицательное число, THEN THE Validator SHALL отображать сообщение об ошибке под полем MonthlyTopUp и блокировать переход на ResultScreen.

---

### Requirement 4: Расчёт итоговой суммы

**User Story:** Как пользователь, я хочу получить корректный расчёт итоговой суммы и начисленных процентов, чтобы оценить доходность вклада.

#### Acceptance Criteria

1. WHEN пользователь нажимает «Рассчитать» на InputStep2Screen, THE Calculator SHALL вычислить FinalAmount по формуле сложных процентов с учётом ежемесячного пополнения за каждый месяц срока.
2. WHEN MonthlyTopUp не указан или равен нулю, THE Calculator SHALL вычислить FinalAmount только на основе InitialAmount, PeriodMonths и InterestRate.
3. THE Calculator SHALL вычислить InterestEarned как разность FinalAmount и суммы всех взносов (InitialAmount + MonthlyTopUp × PeriodMonths).
4. THE Calculator SHALL округлить FinalAmount и InterestEarned до двух знаков после запятой.

---

### Requirement 5: Экран результата

**User Story:** Как пользователь, я хочу видеть полную информацию о результате расчёта, чтобы принять решение о сохранении.

#### Acceptance Criteria

1. THE ResultScreen SHALL отображать карточку с полями: InitialAmount, PeriodMonths, InterestRate, MonthlyTopUp, FinalAmount, InterestEarned.
2. WHEN MonthlyTopUp не был указан, THE ResultScreen SHALL отображать значение MonthlyTopUp как «0» или «Не указано».
3. THE ResultScreen SHALL содержать кнопку «Сохранить», при нажатии на которую THE DepositRepository SHALL сохранить расчёт в AppDatabase с текущей датой и временем.
4. WHEN сохранение выполнено успешно, THE ResultScreen SHALL отображать уведомление (Snackbar или Toast) «Расчёт сохранён».
5. THE ResultScreen SHALL содержать кнопку «В начало», при нажатии на которую выполняется переход на MainScreen.
6. WHILE пользователь находится на ResultScreen, THE ResultViewModel SHALL сохранять данные результата при повороте экрана.

---

### Requirement 6: История расчётов

**User Story:** Как пользователь, я хочу просматривать список сохранённых расчётов, чтобы сравнивать результаты.

#### Acceptance Criteria

1. THE HistoryScreen SHALL отображать список всех сохранённых расчётов из AppDatabase, отсортированных по дате в порядке убывания (новые — первыми).
2. THE HistoryScreen SHALL отображать для каждого элемента списка: дату расчёта, InitialAmount и FinalAmount.
3. WHEN пользователь нажимает на элемент списка, THE HistoryScreen SHALL выполнять переход на DetailsScreen с передачей идентификатора выбранного расчёта.
4. WHEN список расчётов пуст, THE HistoryScreen SHALL отображать сообщение «История расчётов пуста».
5. WHILE пользователь находится на HistoryScreen, THE HistoryViewModel SHALL обновлять список при добавлении новых расчётов в AppDatabase.

---

### Requirement 7: Детальный просмотр расчёта

**User Story:** Как пользователь, я хочу видеть полную информацию по выбранному расчёту из истории, чтобы ознакомиться с его параметрами.

#### Acceptance Criteria

1. THE DetailsScreen SHALL отображать все поля выбранного DepositCalculation: дату расчёта, InitialAmount, PeriodMonths, InterestRate, MonthlyTopUp, FinalAmount, InterestEarned.
2. WHEN пользователь нажимает кнопку «Назад», THE DetailsScreen SHALL выполнять переход обратно на HistoryScreen.
3. WHILE пользователь находится на DetailsScreen, THE DetailsViewModel SHALL сохранять данные расчёта при повороте экрана.

---

### Requirement 8: Хранение данных

**User Story:** Как пользователь, я хочу, чтобы история расчётов сохранялась между сессиями приложения, чтобы не терять результаты.

#### Acceptance Criteria

1. THE AppDatabase SHALL быть реализована как Singleton и инициализироваться единожды за жизненный цикл приложения.
2. THE DepositDao SHALL предоставлять операции вставки нового DepositCalculation и получения всех DepositCalculation, отсортированных по дате убывания.
3. THE DepositRepository SHALL выполнять все операции с AppDatabase в фоновом потоке (coroutine или IO dispatcher).
4. THE DepositCalculation SHALL хранить поля: id (автогенерация), initialAmount, periodMonths, interestRate, monthlyTopUp, finalAmount, interestEarned, calculationDate (timestamp в миллисекундах).

---

### Requirement 9: Архитектура и устойчивость

**User Story:** Как разработчик, я хочу, чтобы приложение следовало архитектуре MVVM и было устойчиво к изменениям конфигурации, чтобы обеспечить поддерживаемость и надёжность кода.

#### Acceptance Criteria

1. THE App SHALL реализовывать архитектурный паттерн MVVM: каждый экран имеет соответствующий ViewModel, который не хранит ссылок на View.
2. THE App SHALL использовать паттерн Repository для разделения логики доступа к данным и бизнес-логики.
3. WHEN происходит поворот экрана или другое изменение конфигурации, THE App SHALL сохранять состояние всех ViewModel без потери данных.
4. THE App SHALL использовать Navigation Component для управления переходами между экранами через единый nav_graph.xml.
5. THE App SHALL соответствовать принципам Material Design: использовать компоненты MaterialButton, TextInputLayout, CardView, RecyclerView.
