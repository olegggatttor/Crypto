SHIFT = 97

VAR = '004.cipher'

cipher = open(VAR).read()
n = len(cipher)
ALPHABET_SIZE = 26

indexes = []
start = 6
end = 11
step_lists = []


# с помощью индекса совпадений определяем длинну ключа (в моем случае это 9)
def get_key_len():
    for j in range(start, end):
        strings_for_indexes = []
        for k in range(0, j):
            line = []
            for p in range(0, n, j):
                if k + p < n:
                    line.append(cipher[k + p])
            strings_for_indexes.append(line)
        step_lists.append(strings_for_indexes)
        cur_len_indexes = []
        for line in strings_for_indexes:
            index = 0
            for i in range(0, ALPHABET_SIZE):
                fi = line.count(chr(i + SHIFT))
                index += fi * (fi - 1) / (len(line) * (len(line) - 1))
            cur_len_indexes.append(round(index, 6))
        indexes.append(cur_len_indexes)
    mapped_indexes = list(map(lambda arr: round(sum(arr) / len(arr), 6), indexes))
    return mapped_indexes.index(max(mapped_indexes))  # + start


# вычисление индекса взаимных совпадений
def get_mutual_index(str1, str2):
    index = 0
    len1 = len(str1)
    len2 = len(str2)
    for i in range(0, ALPHABET_SIZE):
        fi = str1.count(chr(i + SHIFT))
        gi = str2.count(chr(i + SHIFT))
        index += (fi * gi) / (len1 * len2)
    return index


key_len_pos = get_key_len()


# с помощью индекса взаимных совпадений находим относительные сдвиги в ключе и перебираем весь алфавит с этими
# сдвигами, чтобы получить возможные ключи
def find_possible_keys():
    all_keys = []
    relative_shift = []
    for i in range(0, len(indexes[key_len_pos]) - 1):
        str1 = step_lists[key_len_pos][0]
        str2 = step_lists[key_len_pos][i + 1]
        mutual_index_values = []
        for s in range(0, 26):
            shifted_str1 = list(map(lambda x: chr(SHIFT + (ord(x) - SHIFT + s) % ALPHABET_SIZE), str1))
            mutual_index = get_mutual_index(shifted_str1, str2)
            mutual_index_values.append(round(mutual_index, 5))
        best_s = mutual_index_values.index(max(mutual_index_values))
        relative_shift.append(ALPHABET_SIZE - best_s)
    # print('KEYS:')
    for first_letter in range(0, ALPHABET_SIZE):
        key = [chr(SHIFT + first_letter)]
        for cur_letter in range(0, start + key_len_pos - 1):
            key.append(chr(SHIFT + ((first_letter - relative_shift[cur_letter]) % ALPHABET_SIZE)))
        # print(''.join(key))
        all_keys.append(list(key))
    return all_keys


# перебираем все возможные ключи, находим ответ
def decode(keys):
    answers = []
    for k in keys:
        decoded_string = []
        for pos in range(0, n):
            decoded_string.append(chr(SHIFT + ((ord(cipher[pos]) - ord(k[pos % (key_len_pos + start)])) % 26)))
        answers.append(decoded_string)
    return answers


possible_keys = find_possible_keys()

file_ans = open('ans.txt', 'w')
file_ans.write(''.join(decode(possible_keys)[ord('r') - SHIFT]))  # в моем случае верный ключ - riswjhaky
file_ans.close()
