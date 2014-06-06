class HomeController < ApplicationController
  layout false


  def index

    token = 'thisisforrepeatnotice'
    message_json = {:title => '测试 title', :desc => '测试 desc', :other => '测试 other'}
    a_message = token + message_json[:title] + message_json[:desc] + message_json[:other]
    
    return render :nothing => true if has_token_and_message?(a_message)

    title = '测试 title'
    desc = '测试 desc'
    other = '测试 other'

    render :json => message_json

    
    
  end


  def has_token_and_message?(a_message)
    filename = 'token_and_message'
    open(filename, 'a+') do |f|
      f.each_line do |ff|
        ff = ff.tr("\n","")

        while a_message == ff
          # p '此消息已经发送过'
          return true
        end
      end

      
      f.puts a_message
      false
    end
  end

end
