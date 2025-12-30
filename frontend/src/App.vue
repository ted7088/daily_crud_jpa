<template>
  <div id="app">
    <div class="container">
      <header class="app-header">
        <h1>📝 할일 관리</h1>
        <p class="subtitle">JPA & Vue로 만드는 Todo App</p>
      </header>

      <main class="app-main">
        <TodoForm
          :edit-todo="editingTodo"
          @create="handleCreate"
          @update="handleUpdate"
          @cancel="handleCancelEdit"
        />

        <TodoList
          :todos="todos"
          @toggle="handleToggle"
          @edit="handleEdit"
          @delete="handleDelete"
          @delete-multiple="handleDeleteMultiple"
        />
      </main>
    </div>
  </div>
</template>

<script>
import { todoService } from './services/todoService';
import TodoForm from './components/TodoForm.vue';
import TodoList from './components/TodoList.vue';

export default {
  name: 'App',
  components: {
    TodoForm,
    TodoList
  },
  data() {
    return {
      todos: [],
      editingTodo: null
    };
  },
  created() {
    this.loadTodos();
  },
  methods: {
    async loadTodos() {
      try {
        const response = await todoService.getAllTodos();
        this.todos = response.data;
      } catch (error) {
        console.error('할일 목록 로드 실패:', error);
        alert('할일 목록을 불러오는데 실패했습니다.');
      }
    },

    async handleCreate(todoData) {
      try {
        await todoService.createTodo(todoData);
        await this.loadTodos();
        alert('할일이 추가되었습니다! ✅');
      } catch (error) {
        console.error('할일 생성 실패:', error);
        alert('할일 추가에 실패했습니다.');
      }
    },

    async handleUpdate(todoData) {
      try {
        await todoService.updateTodo(this.editingTodo.id, todoData);
        await this.loadTodos();
        this.editingTodo = null;
        alert('할일이 수정되었습니다! ✏️');
      } catch (error) {
        console.error('할일 수정 실패:', error);
        alert('할일 수정에 실패했습니다.');
      }
    },

    async handleToggle(todoId) {
      try {
        await todoService.toggleTodo(todoId);
        await this.loadTodos();
      } catch (error) {
        console.error('할일 상태 변경 실패:', error);
        alert('할일 상태 변경에 실패했습니다.');
      }
    },

    handleEdit(todo) {
      this.editingTodo = { ...todo };
      window.scrollTo({ top: 0, behavior: 'smooth' });
    },

    handleCancelEdit() {
      this.editingTodo = null;
    },

    async handleDelete(todoId) {
      if (!confirm('정말 삭제하시겠습니까?')) {
        return;
      }

      try {
        await todoService.deleteTodo(todoId);
        await this.loadTodos();
        alert('할일이 삭제되었습니다! 🗑️');
      } catch (error) {
        console.error('할일 삭제 실패:', error);
        alert('할일 삭제에 실패했습니다.');
      }
    },

    async handleDeleteMultiple(todoIds) {
      if (!confirm(`선택한 ${todoIds.length}개의 할일을 삭제하시겠습니까?`)) {
        return;
      }

      try {
        await todoService.deleteMultipleTodos(todoIds);
        await this.loadTodos();
        alert(`${todoIds.length}개의 할일이 삭제되었습니다! 🗑️`);
      } catch (error) {
        console.error('일괄 삭제 실패:', error);
        alert('일괄 삭제에 실패했습니다.');
      }
    }
  }
};
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  min-height: 100vh;
  padding: 2rem 1rem;
}

#app {
  max-width: 100%;
}

.container {
  max-width: 800px;
  margin: 0 auto;
}

.app-header {
  text-align: center;
  margin-bottom: 2rem;
  color: white;
}

.app-header h1 {
  font-size: 2.5rem;
  margin-bottom: 0.5rem;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
}

.subtitle {
  font-size: 1.1rem;
  opacity: 0.9;
}

.app-main {
  animation: fadeIn 0.5s ease-in;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 768px) {
  body {
    padding: 1rem 0.5rem;
  }

  .app-header h1 {
    font-size: 2rem;
  }

  .subtitle {
    font-size: 1rem;
  }
}
</style>
