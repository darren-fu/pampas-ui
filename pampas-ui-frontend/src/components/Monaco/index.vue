<template>
  <div :id="container_id" class="monaco_container"></div>
</template>

<script>
  import * as monaco from 'monaco-editor';

  export default {
    name: "monaco",
    props: {
      type: {type: String, default: "yaml"},
      content: {type: String, default: undefined},
      fontSize: {type: Number, default: 15},
    },
    data() {
      return {
        container_id: 'monaco_' + Date.now() + this.type,
        monaco_editor: undefined,
        changed: false,
      }
    },
    mounted() {
      monaco.editor.defineTheme('myTheme', {
        // base: 'vs-dark',
        base: 'hc-black',
        inherit: true,
        rules: [],
        colors: {
          'editor.lineHighlightBackground': '#424242',
          'editorLineNumber.foreground': '#ffffff',
          'editor.selectionBackground': '#424242',
        }
      });
      this.monaco_editor = monaco.editor.create(document.getElementById(this.container_id), {
        value: this.content,
        language: this.type,
        scrollBeyondLastLine: false,
        theme: "hc-black",
        fontSize: this.fontSize
      });
      this.monaco_editor.onDidChangeModelContent(e => {
        this.changed = true
      })
    },
    created() {
    },
    watch: {
      content: function (newVal, oldVal) {
        if (typeof (newVal) != 'undefined') {
          this.setContent(newVal)
        }
      }
    },
    methods: {
      setContent(content) {
        this.monaco_editor.setValue(content)
      },
      getContent() {
        return this.monaco_editor.getValue()
      },
      isChanged() {
        return this.changed;
      },
      resetChanged() {
        this.changed = false
      }
    }
  }
</script>

<style>

  .monaco_container {
    min-height: calc(100vh - 230px);
  }
</style>
