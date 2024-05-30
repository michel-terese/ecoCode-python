import re

string = 'abc.txt'

# ---- string starts with ...

# re.search()

if re.search(r'^abc', string):   # Noncompliant {{Use startswith instead of regex}}
    print('string starts with abc')

if re.search(r'^abc\.txt', string):   # Noncompliant {{Use startswith instead of regex}}
    print('string starts with abc.txt')

if re.search(r'^...txt', string):   # Compliant
    print('string starts with pattern ???txt')

if re.search(r'^abc\*', string):   # Noncompliant {{Use startswith instead of regex}}
    print('string starts with the text "abc*"')

if re.search(r'^abc*', string):   # Compliant
    print('string starts with pattern')

if re.search(r'^abc\+', string):   # Noncompliant {{Use startswith instead of regex}}
    print('string starts with the text "abc+"')

if re.search(r'^abc+', string):   # Compliant
    print('string starts with pattern')

if re.search('^abc', string):    # Noncompliant {{Use startswith instead of regex}}
   print('string starts with abc')

# if re.search(r'\Aabc', string):  # Noncompliant {{Use startswith instead of regex}}
#     print('string starts with abc')

# if re.search('\\Aabc', string):  # Noncompliant {{Use startswith instead of regex}}
#     print('string starts with abc')

# # re.match()

if re.match(r'^abc', string):    # Noncompliant {{Use startswith instead of regex}}
    print('string starts with abc')

if re.match('^abc', string):     # Noncompliant {{Use startswith instead of regex}}
    print('string starts with abc')

# if re.match(r'\Aabc', string):   # Noncompliant {{Use startswith instead of regex}}
#     print('string starts with abc')

# if re.match('\\Aabc', string):   # Noncompliant {{Use startswith instead of regex}}
#     print('string starts with abc')

# # re.match() matches *only* at the beginning of the string, therefore the pattern 'abc' produce the same result as '^abc' or '\Aabc'
# if re.match(r'abc', string):     # Noncompliant {{Use startswith instead of regex}}
#     print('string starts with abc')

# if re.match('abc', string):      # Noncompliant {{Use startswith instead of regex}}
#     print('string starts with abc')

# # re.compile()

pattern = re.compile(r'^abc')           # Noncompliant {{Use startswith instead of regex}}
pattern = re.compile(r'\Aabc')        # Noncompliant {{Use startswith instead of regex}}
pattern = re.compile('^abc')          # Noncompliant {{Use startswith instead of regex}}
# pattern = re.compile('\\Aabc')        # Noncompliant {{Use startswith instead of regex}}
pattern = re.compile(r'\Aabc\.txt')   # Noncompliant {{Use startswith instead of regex}}
# pattern = re.compile('\\Aabc\*')      # Noncompliant {{Use startswith instead of regex}}
pattern = re.compile('^C\+\+')        # Noncompliant {{Use startswith instead of regex}}
pattern = re.compile('^C\++')         # Compliant
if pattern.search(string):
     print('string starts with abc')

# pattern = re.compile(r'abc')     # Noncompliant {{Use startswith instead of regex}}
# pattern = re.compile('abc')      # Noncompliant {{Use startswith instead of regex}}
# if pattern.match(string):
#     print('string starts with abc')

if pattern.search(string):
    print('string contains abc')

if string.startswith('abc'):     # Compliant
    print('string starts with abc')

# # ---- string ends with ...

# re.search()

if re.search(r'def$', string):   # Noncompliant {{Use endsWith instead of regex}}
    print('string ends with def')

if re.search(r'def\Z', string):  # Noncompliant {{Use endsWith instead of regex}}
    print('string ends with def')

if re.search('def\\Z', string):  # Noncompliant {{Use endsWith instead of regex}}
    print('string ends with def')

# re.compile()

pattern = re.compile(r'def$')   # Noncompliant {{Use endsWith instead of regex}}
pattern = re.compile('def$')    # Noncompliant {{Use endsWith instead of regex}}
pattern = re.compile(r'def\Z')  # Noncompliant {{Use endsWith instead of regex}}
pattern = re.compile('def\\Z')  # Noncompliant {{Use endsWith instead of regex}}
if pattern.search(string):
    print('string ends with def')

if string.endswith('def'):      # Compliant
    print('string ends with def')

# # ---- string contains ...

# if re.search(r'cde', string):  # Noncompliant {{Use the in operator instated}}
#     print('string contains cde')

# if re.search('cde', string):   # Noncompliant {{Use the in operator instated}}
#     print('string contains cde')

# pattern = re.compile(r'cde')   # Noncompliant {{Use string.startswith or in operator instead}}
# pattern = re.compile('cde')    # Noncompliant {{Use string.startswith or in operator instead}}
# if pattern.search(string):
#     print('string contains cde')

# if 'cde' in string:            # Compliant
#     print('string contains cde')

