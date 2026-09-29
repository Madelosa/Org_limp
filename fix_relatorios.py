import re

filepath = r'C:\Users\pichau\Desktop\PROJETO ORG_LIMP\projeto_real\Org_limp\src\main\resources\templates\pages\gerente\relatorios.html'

with open(filepath, 'r', encoding='utf-8') as f:
    content = f.read()

# Replace the problematic tbody section with a version that uses nested loops
old_pattern = r'<tbody>\s*<tr th:each="sup : \$\{supervisores\}">\s*<td th:text="\$\{sup\.nome\}">Nome</td>\s*<td th:text="\$\{#lists\.size\(tarefas\.\?\[supervisorId == sup\.id\]\)\}">0</td>\s*<td th:text="\$\{#lists\.size\(tarefas\.\?\[supervisorId == sup\.id and status\.name\(\) == \'concluida\'\]\)\}">0</td>\s*<td th:text="\$\{#lists\.size\(tarefas\.\?\[supervisorId == sup\.id and status\.name\(\) != \'concluida\'\]\)\}">0</td>\s*</tr>\s*</tbody>'

new_content = '''<tbody>
                    <tr th:each="sup : ${supervisores}">
                        <td th:text="${sup.nome}">Nome</td>
                        <td th:text="${#lists.size(tarefas.?[supervisorId == sup.id])}">0</td>
                        <td th:text="${#lists.size(tarefas.?[supervisorId == sup.id and status.name() == 'concluida'])}">0</td>
                        <td th:text="${#lists.size(tarefas.?[supervisorId == sup.id and status.name() != 'concluida'])}">0</td>
                    </tr>
                </tbody>'''

content = re.sub(old_pattern, new_content, content, flags=re.DOTALL)

with open(filepath, 'w', encoding='utf-8') as f:
    f.write(content)

print('File updated successfully')
