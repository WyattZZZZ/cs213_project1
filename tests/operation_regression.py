"""Compile and verify terminal commands without third-party test dependencies."""
from pathlib import Path
import subprocess
import difflib
import tempfile

ROOT = Path(__file__).resolve().parents[1]
START = 'Parking Management System is in operation.'
END = 'Parking Management System is terminated.'


def run(classes, commands):
    result = subprocess.run(
        ['java', '-cp', classes, 'parking.RunProject1'],
        input=commands, text=True, capture_output=True, check=True)
    assert not result.stderr, result.stderr
    return result.stdout.splitlines()


def expect(classes, commands, lines):
    actual = run(classes, commands + '\nQ\nTHIS MUST NOT RUN\n')
    expected = [START, ''] + lines + [END]
    assert actual == expected, '\n'.join(difflib.unified_diff(
        expected, actual, fromfile='expected', tofile='actual'))


with tempfile.TemporaryDirectory(prefix='parking-tests-') as classes:
    subprocess.run(['javac', '-d', classes,
                    *map(str, (ROOT / 'src/parking').glob('*.java')),
                    str(ROOT / 'tests/ModelRegression.java')], check=True)
    model = subprocess.run(['java', '-cp', classes, 'parking.ModelRegression'],
                           text=True, capture_output=True, check=True)
    assert not model.stdout and not model.stderr, 'Model validation produced terminal output'
    for testbed in ['Date', 'Timestamp']:
        result = subprocess.run(['java', '-cp', classes, 'parking.' + testbed],
                                text=True, capture_output=True, check=True)
        assert 'FAIL' not in result.stdout, result.stdout
    print('PASS: model state, silent validation, Date and Timestamp testbeds')
    expect(classes, '\nPP\nPD\nPH\na\nO A100\nC XXX', [
        'Vehicle list is empty - no vehicle is registered.',
        'Deck list is empty - no deck is open.',
        'Deck list is empty - no deck is open.',
        'a is an invalid command!',
        'A100 - invalid deck number.',
        'XXX - invalid deck number; it contains characters.'])

    setup = 'O 110 Princeton HR6 1\nO 200 Edison HR5 2\nA r48-jik\n'
    setup_output = ['Deck#110 opened.', 'Deck#200 opened.', 'R48-JIK registered.']
    cases = [
        ('E 11x bad bad bad', '11x - invalid deck number; it contains characters.'),
        ('E 999 bad bad bad', 'Deck#999 - does not exist.'),
        ('E 110 bad bad bad', 'bad - invalid license plate format.'),
        ('E 110 A11-AAA bad bad', 'A11-AAA - is not registered.'),
        ('E 110 R48-JIK 2026-02-29 25:60', '2026-02-29 - invalid calendar date.'),
        ('E 110 R48-JIK 2026-03-09 25:60', '25 - invalid hour.'),
        ('E 110 R48-JIK 2026-03-09 10:60', '60 - invalid minute.'),
        ('E 110 R48-JIK 2026-03-09 256:00', '256 - invalid hour.'),
        ('E 110 R48-JIK 2026-03-09 06:29',
         'Error entering - not within the operating hours: 6:30 ~ 18:30'),
        ('E 110 R48-JIK 2026-03-09 18:40',
         'Error entering - not within the operating hours: 6:30 ~ 18:30'),
        ('X bad bad bad', 'bad - invalid license plate format.'),
        ('X A11-AAA bad bad', 'Error exiting - A11-AAA is not in a deck.'),
        ('PH bad', 'bad - invalid license plate format.'),
        ('PH A11-AAA', 'A11-AAA does not exist.'),
        ('PH r48-jik', 'R48-JIK - no parking history.'),
        ('PD xxx', 'xxx - invalid deck number; it contains characters.'),
        ('PD 999', 'Deck#999 - does not exist.'),
        ('O 300 Clark BAD 2', 'BAD - invalid operation hours'),
        ('O 300 Clark HR5 7', '7 - exceeds the maximum deck capacity 6'),
    ]
    for command, output in cases:
        expect(classes, setup + command, setup_output + [output])

    expect(classes, setup + '''E 110 r48-jik 2026-03-09 10:48
E 110 bad bad bad
E 200 R48-JIK bad bad
X R48-JIK 2026-02-29 25:60
X R48-JIK 2026-03-09 25:60
X R48-JIK 2026-03-09 10:60
X R48-JIK 2026-03-08 06:29
X R48-JIK 2026-03-09 10:47
X R48-JIK 2026-03-11 10:49
X r48-jik 2026-03-11 10:48
PH r48-jik
PD 110''', setup_output + [
        'R48-JIK entered Deck#110 on 2026-03-09 10:48',
        'Deck#110 - is full.',
        'Error entering - R48-JIK is already in a deck.',
        '2026-02-29 - invalid calendar date.', '25 - invalid hour.',
        '60 - invalid minute.',
        'Error exiting - not within the operating hours: 6:30 ~ 18:30',
        'Exiting time 2026-03-09 10:47 before entering time 2026-03-09 10:48',
        'Invalid exiting time - exceeds two days.',
        'R48-JIK exited Deck#110 on 2026-03-11 10:48',
        '** Parking history for R48-JIK**',
        'R48-JIK [entered: 2026-03-09 10:48][exited: 2026-03-11 10:48]',
        '** end of list **',
        '** List of vehicles in Deck# 110, ordered by plate **',
        '** end of list **'])

    for date, next_date in [('2024-02-28', '2024-03-01'),
                            ('2026-02-28', '2026-03-02'),
                            ('2026-12-31', '2027-01-02')]:
        expect(classes, setup + f'E 110 R48-JIK {date} 18:30\n'
               f'X R48-JIK {next_date} 18:30', setup_output + [
                   f'R48-JIK entered Deck#110 on {date} 18:30',
                   f'R48-JIK exited Deck#110 on {next_date} 18:30'])

    expect(classes, setup + 'C 110\nE 110 bad bad bad\nPD 110\nO 110\nPD',
           setup_output + ['Deck#110 - closed.',
                           'Deck#110 - is closed for parking.',
                           'Deck#110 - is closed for parking.',
                           'Deck#110 - was closed, now reopened.',
                           '** List of decks, ordered by county/deck number **',
                           'Deck#110@PRINCETON[open 6:30 ~ 18:30] [capacity 1] [0 vehicles] [Mercer]',
                           'Deck#200@EDISON[open 5:00 ~ 21:30] [capacity 2] [0 vehicles] [Middlesex]',
                           '** end of list **'])

    expect(classes, setup + """A r48-jik
R bad
R A11-AAA
E 110 R48-JIK 2026-03-09 06:30
R r48-jik
X R48-JIK 2026-03-09 06:30
R r48-jik
A A11-AAA
R a11-aaa
PP""", setup_output + [
        'r48-jik is already registered.',
        'bad - invalid license plate format.',
        'A11-AAA does not exist; cannot be unregistered.',
        'R48-JIK entered Deck#110 on 2026-03-09 06:30',
        'Cannot be unregistered - r48-jik is in a deck.',
        'R48-JIK exited Deck#110 on 2026-03-09 06:30',
        'Cannot be unregistered - r48-jik has parking history.',
        'A11-AAA registered.', 'A11-AAA unregistered.',
        '** List of registered vehicles, ordered by license plate **',
        'R48-JIK', '** end of list **'])

    # Grow both arrays, sort them, remove a middle parking, and reuse its space.
    commands = []
    for index in range(6, 0, -1):
        commands += [f'O {index} Edison HR5 6', f'A A0{index}-AAA']
    commands += ['PP', 'PD']
    for index in range(6, 0, -1):
        commands += [f'E 3 A0{index}-AAA 2026-03-09 05:00']
    commands += ['PD 3', 'X A03-AAA 2026-03-09 06:00',
                 'E 3 A03-AAA 2026-03-10 05:00',
                 'X A03-AAA 2026-03-10 06:00', 'PH', 'Q']
    lines = run(classes, '\n'.join(commands))
    start = lines.index('** List of registered vehicles, ordered by license plate **')
    assert lines[start + 1:start + 7] == [f'A0{i}-AAA' for i in range(1, 7)]
    start = lines.index('** List of decks, ordered by county/deck number **')
    assert [line.split('@')[0] for line in lines[start + 1:start + 7]] == [
        f'Deck#{i}' for i in range(1, 7)]
    start = lines.index('** List of vehicles in Deck# 3, ordered by plate **')
    assert [line.split()[0] for line in lines[start + 1:start + 7]] == [
        f'A0{i}-AAA' for i in range(1, 7)]
    assert lines[-5:] == [
        '** Parking history for all vehicles, ordered by plate/timestamp **',
        'A03-AAA [entered: 2026-03-10 05:00][exited: 2026-03-10 06:00]',
        'A03-AAA [entered: 2026-03-09 05:00][exited: 2026-03-09 06:00]',
        '** end of parking history **', END]

    for command in ['A', 'R', 'E', 'X', 'O', 'C',
                    'E 999999999999999999999 R48-JIK 2026-03-09 10:00']:
        run(classes, command + '\nQ\n')
    # Use the supplied input sequence unchanged, including text following Q.
    official_input = (ROOT / 'Project1TestCases.txt').read_text()
    official_expected = (ROOT / 'Project1Output.txt').read_text().splitlines()
    official_actual = run(classes, official_input)
    assert official_actual == official_expected, '\n'.join(difflib.unified_diff(
        official_expected, official_actual, fromfile='official', tofile='actual'))
    print('PASS: complete official input/output comparison')

    print('PASS: validation order, exact output, hours, 48-hour limit, sorting, growth, history, Q')
